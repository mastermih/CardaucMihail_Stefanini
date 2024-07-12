package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.enumobects.Status;

import java.sql.SQLException;
import java.time.LocalDateTime;

public interface OrdersService {
    Paginable<Order> findPaginableOrderByCreatedDate(LocalDateTime createdDate, Long numberOfOrders, Long page) throws SQLException;
    Paginable<Order> findPaginableOrderByCreatedDateAndStatus(LocalDateTime createdDate, Status status, Long numberOfOrders, Long page) throws SQLException;
    LocalDateTime findLastCreatedDate()throws SQLException;
}
