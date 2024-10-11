package com.ImperioElevator.ordermanagement.dao;

import com.ImperioElevator.ordermanagement.entity.UserNotification;

import java.sql.SQLException;

public interface UserNotificationDao extends Dao<UserNotification> {
    Long insertUserNotification(UserNotification entity) throws SQLException;
    Long notificationIsRead(Long userId) throws SQLException;
    Long notificationIsDisabled(Long notificationId, Long userId) throws SQLException;
}
