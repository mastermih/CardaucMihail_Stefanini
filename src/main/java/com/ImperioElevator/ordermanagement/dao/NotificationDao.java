package com.ImperioElevator.ordermanagement.dao;

import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.Order;

import java.sql.SQLException;
import java.util.List;

public interface NotificationDao extends Dao<Notification> {

    List<Notification> getNotificationsOfCustomerCreateOrder(Long userId)throws SQLException;
}
