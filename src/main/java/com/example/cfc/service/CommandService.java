package com.example.cfc.service;

import com.example.cfc.data.DataAnswer;
import com.example.cfc.model.mongo.Schedule;
import com.example.cfc.model.postgre.Notification;
import com.example.cfc.model.postgre.User;
import com.example.cfc.repository.mongo.ScheduleRepository;
import com.example.cfc.repository.postgre.NotificationRepository;
import com.example.cfc.repository.postgre.UserRepository;
import org.bson.Document;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommandService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public CommandService(NotificationRepository notificationRepository,
                          UserRepository userRepository,
                          ScheduleRepository scheduleRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public String start(Message message) {
        if (checkRegistration(message.getFrom().getId())) {
            return DataAnswer.USER_EXIST;
        }

        userRepository.save(new User(message.getFrom().getId(),
                message.getChatId(),
                message.getFrom().getUserName(),
                LocalDate.now()));

        notificationRepository.save(new Notification(message.getFrom().getId()));

        return DataAnswer.USER_CREATE;
    }

    public ArrayList<Schedule> notifyLabs() {
        ArrayList<Schedule> notifySchedule = new ArrayList<>();

        LocalTime time = LocalTime.now();

        ArrayList<Long> ids = notificationRepository.findUserIdsByNotificationType(String.valueOf(time.getHour()));

        for (Long id : ids) {
            Optional<Schedule> schedule = scheduleRepository.findById(id);
            schedule.ifPresent(notifySchedule::add);
        }
        return notifySchedule;
    }

    public String getLabs(Message message) {
        if (!checkRegistration(message.getFrom().getId())) {
            return DataAnswer.USER_NOT_EXIST;
        }

        Optional<Schedule> schedule = scheduleRepository.findById(message.getFrom().getId());

        return schedule.map(value -> scheduleFormatter(value.getData())).orElse(DataAnswer.SCHEDULE_NOT_FOUND);
    }

    public String addLab(Message message) {
        if (!checkRegistration(message.getFrom().getId())) {
            return DataAnswer.USER_NOT_EXIST;
        }
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(message.getFrom().getId());

        if (optionalSchedule.isEmpty()) {
            return DataAnswer.SCHEDULE_NOT_FOUND;
        }

        Schedule schedule = optionalSchedule.get();
        ArrayList<Document> documents = schedule.getData();

        String line = message.getText();
        String[] parts = line.split(":");
        String labName = parts[0].trim();
        String labNumber = parts[1].trim();

        ArrayList<Document> newDocuments = new ArrayList<>();
        boolean change = false;
        for (Document doc : documents) {
            if (doc.get("lab_name").equals(labName)) {
                ArrayList<Integer> labNumbers = new ArrayList<>(doc.getList("lab_numbers", Integer.class));
                if (!labNumbers.contains(Integer.parseInt(labNumber))) {
                    labNumbers.add(Integer.parseInt(labNumber));
                    doc.put("lab_numbers", labNumbers);
                    change = true;
                }
            }
            newDocuments.add(doc);
        }

        if (change) {
            schedule.setData(newDocuments);
            scheduleRepository.save(schedule);
            return DataAnswer.LAB_SUCCESS_CREATE;
        }

        return DataAnswer.LAB_EXIST;

    }

    public String addLabs(Message message) {
        if (!checkRegistration(message.getFrom().getId())) {
            return DataAnswer.USER_NOT_EXIST;
        }

        String userSchedule = message.getText();
        Long userId = message.getFrom().getId();

        if (scheduleRepository.findById(userId).isPresent()) {
            return DataAnswer.SCHEDULE_EXIST;
        }
        ArrayList<Document> labs = new ArrayList<>();

        String[] lines = userSchedule.split("\n");

        for (String line : lines) {
            String[] parts = line.split(":");

            String name = parts[0].trim();

            String numbersPart = parts[1].trim();
            String[] numberStrings = numbersPart.split(",");

            List<Integer> numbers = new ArrayList<>();
            for (String numberString : numberStrings) {
                numbers.add(Integer.parseInt(numberString.trim()));
            }
            Document lab = new Document("lab_name", name).append("lab_numbers", numbers);
            labs.add(lab);
        }

        scheduleRepository.save(new Schedule(userId, labs));
        return DataAnswer.SCHEDULE_CREATE;
    }

    public String deleteLab(Message message) {
        if (!checkRegistration(message.getFrom().getId())) {
            return DataAnswer.USER_NOT_EXIST;
        }

        Optional<Schedule> optionalSchedule = scheduleRepository.findById(message.getFrom().getId());

        if (optionalSchedule.isEmpty()) {
            return DataAnswer.SCHEDULE_NOT_FOUND;
        }

        Schedule schedule = optionalSchedule.get();
        ArrayList<Document> documents = schedule.getData();

        String line = message.getText();
        String[] parts = line.split(":");
        String labName = parts[0].trim();
        String labNumber = parts[1].trim();

        ArrayList<Document> newDocuments = new ArrayList<>();
        boolean change = false;
        for (Document doc : documents) {
            if (doc.get("lab_name").equals(labName)) {
                ArrayList<Integer> labNumbers = new ArrayList<>(doc.getList("lab_numbers", Integer.class));
                if (labNumbers.contains(Integer.parseInt(labNumber))) {
                    labNumbers.removeIf(item -> item == Integer.parseInt(labNumber));
                    doc.put("lab_numbers", labNumbers);
                    change = true;
                }
            }
            newDocuments.add(doc);
        }

        if (change) {
            schedule.setData(newDocuments);
            scheduleRepository.save(schedule);
            return DataAnswer.LAB_SUCCESS_DELETE;
        }

        return DataAnswer.LAB_NOT_EXIST;
    }

    public String deleteLabs(Message message) {
        if (!checkRegistration(message.getFrom().getId())) {
            return DataAnswer.USER_NOT_EXIST;
        }

        scheduleRepository.deleteById(message.getFrom().getId());
        return DataAnswer.DELETE_LABS_MESSAGE;
    }

    public String notificationSetting(Message message) {
        if (!checkRegistration(message.getFrom().getId())) {
            return DataAnswer.USER_NOT_EXIST;
        }

        Optional<Notification> notification = notificationRepository.findNotificationByUserIDTg(message.getFrom().getId());

        String[] lines = message.getText().split("\n");

        try {
            notification.get().setMorningNotification(lines[0].trim());
            notification.get().setEveningNotification(lines[1].trim());
            if (lines[2].trim().equalsIgnoreCase("да")) {
                notification.get().setNotificationOn(true);
            }
            if (lines[2].trim().equalsIgnoreCase("нет")) {
                notification.get().setNotificationOn(false);
            }
            notificationRepository.save(notification.get());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ошибка");
        }

        return DataAnswer.NOTIFICATION_SETTINGS_APPLY;
    }


    private boolean checkRegistration(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent();
    }

    public String scheduleFormatter(ArrayList<Document> document) {
        StringBuilder answer = new StringBuilder();
        for (Document doc : document) {
            answer.append(doc.get("lab_name")).append(" : ").append(doc.get("lab_numbers")).append("\n");
        }
        return answer.toString();
    }

}