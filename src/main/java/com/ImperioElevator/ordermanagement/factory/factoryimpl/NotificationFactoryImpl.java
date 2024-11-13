package com.ImperioElevator.ordermanagement.factory.factoryimpl;

import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.UserNotification;
import com.ImperioElevator.ordermanagement.factory.NotifiactionFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactoryImpl implements NotifiactionFactory {


    @Override
    public Notification createOrderCreationNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        return notification;
    }

    @Override
    public Notification createOrderAssignmentNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        return notification;
    }


    @Override
    public Notification createOrderStatusUpdateNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);

        return notification;
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
