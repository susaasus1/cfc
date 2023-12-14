package com.example.cfc.model.postgre;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "user_tg")
@Entity
public class User {

    @Id
    @Column(name = "user_id_tg")
    private Long userId;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "user_tag")
    private String userTag;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    public User(Long userId, Long chatId, String userTag, LocalDate registrationDate) {
        this.userId = userId;
        this.chatId = chatId;
        this.userTag = userTag;
        this.registrationDate = registrationDate;
    }

    public User() {
    }


    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
}
