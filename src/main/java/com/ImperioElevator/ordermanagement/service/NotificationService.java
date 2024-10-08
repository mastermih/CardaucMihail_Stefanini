package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Notification;

import java.sql.SQLException;
import java.util.List;

public interface NotificationService {
        Long insert(Notification entity) throws SQLException;
        List<Notification> getNotificationsOfCustomerCreateOrder() throws SQLException;
}
