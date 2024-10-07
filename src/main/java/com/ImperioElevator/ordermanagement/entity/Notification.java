package com.ImperioElevator.ordermanagement.entity;

import com.ImperioElevator.ordermanagement.enumobects.NotificationStatus;

public class Notification {
    private Integer notificationId;
    private String message;
    private boolean isRead;
    private NotificationStatus notificationStatus;
    private Long user;

    public Notification(){}

    public Notification(Integer notificationId, String message, boolean isRead, NotificationStatus notificationStatus, Long user) {
        this.notificationId = notificationId;
        this.message = message;
        this.isRead = isRead;
        this.notificationStatus = notificationStatus;
        this.user = user;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
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
}
