package com.ImperioElevator.ordermanagement.entity;

public class UserNotification {
    private Long userId;
    private Long notificationId;
    private Boolean isRead;
    private Boolean isDisabled;

    public UserNotification(){}

    public UserNotification(Long userId, Long notificationId, Boolean isRead, Boolean isDisabled) {
        this.userId = userId;
        this.notificationId = notificationId;
        this.isRead = isRead;
        this.isDisabled = isDisabled;
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

    public Boolean getDisabled() {
        return isDisabled;
    }

    public void setDisabled(Boolean disabled) {
        isDisabled = disabled;
    }
}
