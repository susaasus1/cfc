package com.example.cfc.data;

public final class DataAnswer {
    public static final String USER_EXIST = "Вы уже зарегистрированы.";
    public static final String USER_CREATE = "Вы успешно зарегистрировались.";
    public static final String USER_NOT_EXIST = "Вы не зарегистрированы.";

    public static final String SCHEDULE_NOT_FOUND = "Вы еще не добавляли расписание.";
    public static final String SCHEDULE_EXIST = "Расписание уже существует.";
    public static final String SCHEDULE_CREATE = "Расписание успешно создано.";

    public static final String LAB_NOT_EXIST = "Такой лабораторной не существует.";
    public static final String LAB_EXIST = "Лабораторная уже существует.";
    public static final String LAB_SUCCESS_DELETE = "Лабораторная успешно вычеркнута.";
    public static final String LAB_SUCCESS_CREATE = "Лабораторная успешно добавлена.";

    public static final String NOTIFICATION_SETTINGS_APPLY = "Изменили уведомления.";

    public static final String ADD_LABS_INSTRUCTION = """
            Введите названия предмета и поставьте двоеточие, дальше пронумерованные лабораторные
            Пример:
            СОА : 1, 2, 3, 4            
            ОТВ : 1, 2, 3
            """;
    public static final String ADD_LAB_INSTRUCTION = """
            Введите названия предмета и номер лабораторной
            Пример:
            СОА : 5          
            """;
    public static final String DELETE_LAB_INSTRUCTION = """
            Введите названия предмета и номер лабораторной
            Пример:
            СОА : 5          
            """;
    public static final String NOTIFICATION_INSTRUCTION = """
            Введите время утреннего и вечернего уведомления, а так же оставить уведолмения или нет
            Утренне уведомление может быть только с 1-12, вечернее с 13-0
            Пример:
            8
            23
            да/нет
            """;
    public static final String DELETE_LABS_MESSAGE = "Все лабораторные успешно удалены!";

}