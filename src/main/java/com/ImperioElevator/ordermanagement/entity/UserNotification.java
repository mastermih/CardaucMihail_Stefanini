package com.ImperioElevator.ordermanagement.entity;

public class UserNotification {
    private Long userId;
    private Long notificationId;
    private Boolean isRead;

    public UserNotification(){}

    public UserNotification(Long userId, Long notificationId, Boolean isRead) {
        this.userId = userId;
        this.notificationId = notificationId;
        this.isRead = isRead;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}
