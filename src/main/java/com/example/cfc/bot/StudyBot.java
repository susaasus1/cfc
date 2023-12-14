package com.example.cfc.bot;

import com.example.cfc.config.BotConfig;
import com.example.cfc.data.DataAnswer;
import com.example.cfc.model.mongo.Schedule;
import com.example.cfc.model.postgre.User;
import com.example.cfc.repository.postgre.UserRepository;
import com.example.cfc.service.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
public class StudyBot extends TelegramLongPollingBot {

    enum BotState {
        NOTHING,
        DELETE_LAB,
        ADD_LAB,
        ADD_LABS,
        NOTIFICATION
    }

    private static final Logger logger = LoggerFactory.getLogger(StudyBot.class);

    private Map<Long, BotState> userStates = new HashMap<>();

    private final BotConfig botConfig;
    private final CommandService commandService;
    private final UserRepository userRepository;

    public StudyBot(BotConfig botConfig,
                    CommandService commandService, UserRepository userRepository) {
        this.botConfig = botConfig;
        this.commandService = commandService;
        this.userRepository = userRepository;
        registerCommands();}

    private void registerCommands() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("start", "Команда для начала работы"));
        commands.add(new BotCommand("labs", "Покажет все ваши лабораторные"));
        commands.add(new BotCommand("addlab", "Добавить одну лабораторную работу к существующим"));
        commands.add(new BotCommand("addlabs", "Добавить все лабораторные работы"));
        commands.add(new BotCommand("deletelab", "Удалить лабораторную работу"));
        commands.add(new BotCommand("deletelabs", "Удалить весь список лабораторных работ"));
        commands.add(new BotCommand("notification", "Изменить уведомления"));

        registerBotCommands(commands);

    }

    private void registerBotCommands(List<BotCommand> commands) {
        try {
            SetMyCommands setMyCommands = new SetMyCommands();
            setMyCommands.setCommands(commands);
            execute(setMyCommands);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("Message received: {}", update.toString());
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            String answer = parseCommand(message);
            sendMessage.setText(answer);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public String parseCommand(Message message) {
        switch (message.getText()) {
            case "/start":
                return commandService.start(message);
            case "/labs":
                return commandService.getLabs(message);
            case "/addlabs":
                userStates.put(message.getFrom().getId(), BotState.ADD_LABS);
                return DataAnswer.ADD_LABS_INSTRUCTION;
            case "/addlab":
                userStates.put(message.getFrom().getId(), BotState.ADD_LAB);
                return DataAnswer.ADD_LAB_INSTRUCTION;
            case "/deletelabs":
                return commandService.deleteLabs(message);
            case "/deletelab":
                userStates.put(message.getFrom().getId(), BotState.DELETE_LAB);
                return DataAnswer.DELETE_LAB_INSTRUCTION;
            case "/notification":
                userStates.put(message.getFrom().getId(), BotState.NOTIFICATION);
                return DataAnswer.NOTIFICATION_INSTRUCTION;
            default:
                break;
        }

        BotState botState = userStates.get(message.getFrom().getId());

        if (botState != null) {
            if (botState.equals(BotState.ADD_LABS)) {
                userStates.put(message.getFrom().getId(), BotState.NOTHING);
                return commandService.addLabs(message);
            } else if (botState.equals(BotState.ADD_LAB)) {
                userStates.put(message.getFrom().getId(), BotState.NOTHING);
                return commandService.addLab(message);
            } else if (botState.equals(BotState.DELETE_LAB)) {
                userStates.put(message.getFrom().getId(), BotState.NOTHING);
                return commandService.deleteLab(message);
            } else if (botState.equals(BotState.NOTIFICATION)) {
                userStates.put(message.getFrom().getId(), BotState.NOTHING);
                return commandService.notificationSetting(message);
            }
        }

        return "Такой команды нет!";

    }

    @Scheduled(cron = "0/15 * * * * *")
    public void notifySchedule() {
        ArrayList<Schedule> schedules = commandService.notifyLabs();

        for (Schedule sc : schedules) {
            SendMessage sendMessage = new SendMessage();

            Optional<User> user = userRepository.findById(sc.getId());
            sendMessage.setChatId(user.get().getChatId());

            String answer = commandService.scheduleFormatter(sc.getData());
            sendMessage.setText(answer);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
    }

}
