package com.ImperioElevator.ordermanagement.dao;


//import com.ImperioElevator.ordermanagement.dto.OrdersFinedLastCreatedDTO;
import com.ImperioElevator.ordermanagement.dto.OrdersFoundLastCreatedDTO;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Status;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderDao extends Dao<Order>{
    Paginable<Order> findPaginableOrderByCreatedDate(LocalDateTime startDate,LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException;

    Paginable<Order> findPaginableUserOrderByCreatedDate(Long id, LocalDateTime startDate,LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException;

    Paginable<Order> findPaginableOrderByUpdatedDate(LocalDateTime startDate,LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException;

    Paginable<Order> findPaginableOrderByCreatedDateAndStatus(LocalDateTime startDate,LocalDateTime endDate, Status status, Long numberOfOrders, Long page) throws SQLException;

    List<OrdersFoundLastCreatedDTO> findLastCreatedOrders(Number limit) throws SQLException;


    List<Order> findLastCreatedOrdersForUserRole(Number limit, Long id) throws SQLException;

    Long updateStatus(Order order) throws SQLException;

    String updateOrderEmailConfirmStatus(String token) throws SQLException;

    void disableTokenAfterConfirmation(String token) throws SQLException;

    Order getOrderWithExtraProducts(Long orderId);

    Integer deleteUnconfirmedOrderByEmail()throws SQLException;
    String getTheConfirmationToken(Long id) throws SQLException;

    String assigneeOperatorToOrder(Long id, String name) throws SQLException;
    List<String> finedOperatorByName(String name)throws SQLException;
    List<String> getOperatorAssignedToOrder (Long orderId) throws SQLException;
    Long deleteOperatorAssignedToOrderByOperatorId (Long orderId, Long operator_id) throws SQLException;
}