package com.ImperioElevator.ordermanagement.dao;

import com.ImperioElevator.ordermanagement.entity.UserNotification;

import java.sql.SQLException;

public interface UserNotificationDao extends Dao<UserNotification> {
    Long insertUserNotification(UserNotification entity) throws SQLException;
}
