package com.ImperioElevator.ordermanagement.entity;

import java.time.LocalDateTime;
import java.util.List;

//ToDO realtion many to many with user
public class Notification {
    private Long notificationId;
    private String message;
    private LocalDateTime createdDate;
    private List<UserNotification> userNotifications;

    public Notification(){}

    public Notification(Long notificationId, String message, LocalDateTime createdDate, List<UserNotification> userNotifications) {
        this.notificationId = notificationId;
        this.message = message;
        this.createdDate = createdDate;
        this.userNotifications = userNotifications;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<UserNotification> getUserNotifications() {
        return userNotifications;
    }

    public void setUserNotifications(List<UserNotification> userNotifications) {
        this.userNotifications = userNotifications;
    }
}
