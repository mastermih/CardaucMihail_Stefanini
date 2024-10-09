package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.UserNotification;

import java.sql.SQLException;
import java.util.List;

public interface NotificationService {
        Long insert(Notification entity) throws SQLException;
        List<Notification> getNotifications(Long userId) throws SQLException;
        Long insertUserNotification(UserNotification userNotification)throws SQLException;

}
