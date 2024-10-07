package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Notification;

import java.sql.SQLException;

public interface NotificationService {
        Long insert(Notification entity) throws SQLException;
}
