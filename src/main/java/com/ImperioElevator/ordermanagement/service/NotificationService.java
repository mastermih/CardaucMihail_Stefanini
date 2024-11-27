package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.UserNotification;
import org.springframework.core.io.ByteArrayResource;

import java.sql.SQLException;
import java.util.List;

public interface NotificationService {
        Long insert(Notification entity) throws SQLException;
        List<Notification> getNotifications(Long userId) ;
        Long insertUserNotification(UserNotification userNotification)throws SQLException;
        Long notificationIsRead(Long userId) throws SQLException;
        Long notificationIsDisabled(Long notificationId, Long userId) throws SQLException;
        Long insertInvoiceNotificationManagement(UserNotification userNotification, ByteArrayResource attachment) throws SQLException;
}
