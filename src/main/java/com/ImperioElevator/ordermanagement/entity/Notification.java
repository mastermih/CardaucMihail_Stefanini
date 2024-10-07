package com.ImperioElevator.ordermanagement.entity;

import com.ImperioElevator.ordermanagement.enumobects.NotificationStatus;
import com.ImperioElevator.ordermanagement.valueobjects.CreateDateTime;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Notification {
    private Long notificationId;
    private String message;
    private boolean isRead;
    private NotificationStatus notificationStatus;
    private Long user;
    private LocalDateTime createdDate;

    public Notification(){}

    public Notification(Long notificationId, String message, boolean isRead, NotificationStatus notificationStatus, Long user, LocalDateTime createdDate) {
        this.notificationId = notificationId;
        this.message = message;
        this.isRead = isRead;
        this.notificationStatus = notificationStatus;
        this.user = user;
        this.createdDate = createdDate;
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

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
