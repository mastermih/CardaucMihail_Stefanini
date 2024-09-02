package com.ImperioElevator.ordermanagement.dao;


import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.valueobjects.Id;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderDao extends Dao<Order>{
    Paginable<Order> findPaginableOrderByCreatedDate(LocalDateTime startDate,LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException;

    Paginable<Order> findPaginableOrderByUpdatedDate(LocalDateTime startDate,LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException;

    Paginable<Order> findPaginableOrderByCreatedDateAndStatus(LocalDateTime startDate,LocalDateTime endDate, Status status, Long numberOfOrders, Long page) throws SQLException;

    List<Order> findLastCreatedOrders(Number limit) throws SQLException;

    Long updateStatus(Order order) throws SQLException;

    Long updateOrderEmailConfirmStatus(Long id) throws SQLException;

    Order getOrderWithExtraProducts(Long orderId);

    Integer deleteUnconfirmedOrderByEmail()throws SQLException;
}