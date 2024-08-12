package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import liquibase.sql.Sql;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface OrdersService {
    Paginable<Order> findPaginableOrderByCreatedDate(LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException;
    Paginable<Order> findPaginableOrderByCreatedDateAndStatus(LocalDateTime startDate, LocalDateTime endDate, Status status, Long numberOfOrders, Long page) throws SQLException;
    List<Order> findLastCreatedOrders(Number limit)throws SQLException;
    Long createOrder(Order order) throws SQLException;
    Long updateOrderStatus(Order order) throws SQLException;
   // Order fiendOrderById(Long id) throws SQLException;

}