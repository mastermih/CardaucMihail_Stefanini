package com.ImperioElevator.ordermanagement.entity;

import org.springframework.core.io.ByteArrayResource;

import java.time.LocalDateTime;
import java.util.List;

public class Notification {
    private Long notificationId;
    private String message;
    private LocalDateTime createdDate;
    private ByteArrayResource attachmentFile;
    private List<UserNotification> userNotifications;

    //Because I use builder I think I can remove it
    public Notification(){}

    public Notification(NotificationBuilder builder) {
        this.notificationId = builder.notificationId;
        this.message = builder.message;
        this.createdDate = builder.createdDate;
        this.userNotifications = builder.userNotifications;
        this.attachmentFile = builder.attachmentFile;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<UserNotification> getUserNotifications() {
        return userNotifications;
    }

    public ByteArrayResource getAttachmentFile(){
        return attachmentFile;
    }

    public void setUserNotifications(List<UserNotification> userNotifications) {
        this.userNotifications = userNotifications;
    }

    public static class NotificationBuilder{
        Long notificationId;
        String message;
        LocalDateTime createdDate;
        ByteArrayResource attachmentFile;
        List<UserNotification> userNotifications;

        public NotificationBuilder attachmentFile(ByteArrayResource attachmentFile){
            this.attachmentFile = attachmentFile;
            return this;
        }

        public NotificationBuilder notificationId(Long notificationId){
            this.notificationId = notificationId;
            return this;
        }

        public NotificationBuilder message(String message){
            this.message = message;
            return this;
        }

        public NotificationBuilder createDate(LocalDateTime createdDate){
            this.createdDate = createdDate;
            return this;
        }

        public NotificationBuilder userNotifications( List<UserNotification> userNotifications){
            this.userNotifications = userNotifications;
            return this;
        }

        public Notification build(){
            return new Notification(this);
        }
    }
}
