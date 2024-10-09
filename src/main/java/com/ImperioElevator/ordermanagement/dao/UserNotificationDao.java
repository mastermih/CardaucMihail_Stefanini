package com.ImperioElevator.ordermanagement.dao;

import com.ImperioElevator.ordermanagement.entity.UserNotification;

import java.sql.SQLException;

public interface UserNotificationDao extends Dao<UserNotification> {
    public Long insertUSerNotification(UserNotification entity) throws SQLException;
}
