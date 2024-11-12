package com.ImperioElevator.ordermanagement.entity;

public class UserNotification {
    private Long userId;
    private Long notificationId;
    private Boolean isRead;
    private Boolean isDisabled;

    public UserNotification(){}

    public UserNotification(UserNotificationBuilder builder) {
        this.userId = builder.userId;
        this.notificationId = builder.notificationId;
        this.isRead = builder.isRead;
        this.isDisabled = builder.isDisabled;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public Boolean getRead() {
        return isRead;
    }

    public Boolean getDisabled() {
        return isDisabled;
    }


public static class UserNotificationBuilder {
    Long userId;
    Long notificationId;
    Boolean isRead = Boolean.FALSE;
    Boolean isDisabled = Boolean.FALSE;

    public UserNotificationBuilder userId(Long userId){
        this.userId = userId;
        return this;
    }

    public UserNotificationBuilder notificationId(Long notificationId){
        this.notificationId = notificationId;
        return this;
    }

    public UserNotificationBuilder isRead(Boolean isRead){
        this.isRead = isRead;
        return  this;
    }

    public UserNotificationBuilder isDisabled(Boolean isDisabled){
        this.isDisabled = isDisabled;
        return this;
    }

    public UserNotification build(){
        return new UserNotification(this);
    }

}
}

