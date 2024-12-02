package com.ImperioElevator.ordermanagement.factory;

import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.entity.UserNotification;
import org.springframework.core.io.ByteArrayResource;


public interface NotifiactionFactory {
    Notification createOrderCreationNotification(String message);

    Notification createInvoiceNotification(String message, ByteArrayResource attachment);

    Notification createOrderAssignmentNotification(String message);

    Notification createOrderStatusUpdateNotification(String message);

    UserNotification createUserNotificationOrderWithProducts(Long notificationId, Long userId);

    UserNotification createUserNotificationAssigneeOperatorToOrder(Long notificationId, Long userId);

    UserNotification createUserNotificationAssineOrderToMe(Long notificationId, Long userId);
}


