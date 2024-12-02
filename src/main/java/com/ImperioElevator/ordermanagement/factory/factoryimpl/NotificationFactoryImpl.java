package com.ImperioElevator.ordermanagement.factory.factoryimpl;

import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.UserNotification;
import com.ImperioElevator.ordermanagement.factory.NotifiactionFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactoryImpl implements NotifiactionFactory {


    @Override
    public Notification createOrderCreationNotification(String message) {
       return new Notification.NotificationBuilder()
               .message(message)
               .build();
    }

    @Override
    public Notification createInvoiceNotification(String message, ByteArrayResource attachment) {
        return new Notification.NotificationBuilder()
                .message(message)
                .attachmentFile(attachment)
                .build();
    }

    @Override
    public Notification createOrderAssignmentNotification(String message) {
        return new Notification.NotificationBuilder()
                .message(message)
                .build();
    }


    @Override
    public Notification createOrderStatusUpdateNotification(String message) {
        return new Notification.NotificationBuilder()
                .message(message)
                .build();
    }

    // UserNotification
    //1
    public UserNotification createUserNotificationOrderWithProducts(Long notificationId, Long userId) {
        return new UserNotification.UserNotificationBuilder()
                .notificationId(notificationId)
                .userId(userId)
                .build();
    }

    //2
    @Override
    public UserNotification createUserNotificationAssigneeOperatorToOrder(Long notificationId, Long userId) {
        return new UserNotification.UserNotificationBuilder()
                .notificationId(notificationId)
                .userId(userId)
                .build();
    }

    //3
    @Override
    public UserNotification createUserNotificationAssineOrderToMe(Long notificationId, Long userId) {
        return new UserNotification.UserNotificationBuilder()
                .notificationId(notificationId)
                .userId(userId)
                .build();
    }
}
