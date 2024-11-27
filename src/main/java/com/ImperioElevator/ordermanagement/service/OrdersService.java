package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.dto.OrdersFoundLastCreatedDTO;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface OrdersService {

    Long createOrderWithProducts(Order order, List<OrderProduct> orderProducts) throws SQLException;

    Paginable<OrdersFoundLastCreatedDTO> findPaginableOrderByCreatedDate(LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException;
    Paginable<Order> findPaginableUserOrderByCreatedDate(Long id, LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException;
    Paginable<OrdersFoundLastCreatedDTO> findPaginableOrderByCreatedDateAndStatus(LocalDateTime startDate, LocalDateTime endDate, Status status, Long numberOfOrders, Long page) throws SQLException;
    List<OrdersFoundLastCreatedDTO> findLastCreatedOrders(Number limit)throws SQLException;
    List<Order> findLastCreatedOrdersForUserRole(Number limit, Long id)throws SQLException;
    Long createOrder(Order order) throws SQLException;

    Long updateOrderStatus(Order order) throws SQLException;
    Long updateOrderStatusReadyForPayment(Order order, String jwtToken) throws SQLException;


    Order fiendOrderById(Long id) throws SQLException;
    Order getOrderWithExtraProducts(Long orderId) throws SQLException;
    String assigneeOperatorToOrder(Long id, String name) throws SQLException;
    List<String> finedOperatorByName (String name) throws SQLException;
    List<String> getOperatorAssignedToOrder (Long orderId) throws SQLException;
    Long deleteOperatorAssignedToOrderByOperatorId (Long orderId, Long operatorId) throws SQLException;
    Long deleteAllOperatorsAssignedToOrderByOperatorId(Long orderId)throws SQLException;
    Long assineOrderToMe(Long orderId, Long operatorId) throws SQLException;
    Long addOrderInvoice(Long orderId, String orderInvoice) throws SQLException;
}