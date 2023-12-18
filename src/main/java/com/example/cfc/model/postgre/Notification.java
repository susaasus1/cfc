package com.example.cfc.model.postgre;


import javax.persistence.*;

@Table(name = "notification")
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id_tg")
    private Long userId;

    @Column(name = "morning_notification")
    private String morningNotification;

    @Column(name = "evening_notification")
    private String eveningNotification;

    @Column(name = "notification_on")
    private boolean notificationOn;

    public Notification(Long userId) {
        this.userId = userId;
        this.morningNotification = "8";
        this.eveningNotification = "20";
        this.notificationOn = true;
    }

    public Notification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMorningNotification() {
        return morningNotification;
    }

    public void setMorningNotification(String morningNotification) {
        this.morningNotification = morningNotification;
    }

    public String getEveningNotification() {
        return eveningNotification;
    }

    public void setEveningNotification(String eveningNotification) {
        this.eveningNotification = eveningNotification;
    }

    public boolean isNotificationOn() {
        return notificationOn;
    }

    public void setNotificationOn(boolean notificationOn) {
        this.notificationOn = notificationOn;
    }
}