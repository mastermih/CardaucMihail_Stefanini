package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.ImperioElevator.ordermanagement.enumobects.Status.READY_FOR_PAYMENT;

@Service
public class PrepareOrderFunction {

    private final OrderDaoImpl orderDao;

    public PrepareOrderFunction(OrderDaoImpl orderDao) {
        this.orderDao = orderDao;
    }

    public final BiFunction<Long, OrderDaoImpl, Order> prepareOrderFunction = (orderId, orderDao) -> {
        try {
            Order orderOperations = orderDao.getOrderWithExtraProducts(orderId);
            orderDao.setOrderStatusToReadyPayment(orderOperations.orderId().id());

            Order updatedOrder = orderDao.getOrderWithExtraProducts(orderId);
            if (!READY_FOR_PAYMENT.equals(updatedOrder.orderStatus())) {
                throw new RuntimeException("Order status update failed.");
            }

            return updatedOrder;
        } catch (Exception e) {
            throw new RuntimeException("Failed to prepare order", e);
        }
    };


    public final BiFunction<Long,OrderDaoImpl, List<String>> fetchOperatorsFunction = (orderId, orderDaoImpl) -> {
        try {
            return orderDaoImpl.getOperatorAssignedToOrder(orderId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch operators for order ID: " + orderId, e);
        }
    };
}