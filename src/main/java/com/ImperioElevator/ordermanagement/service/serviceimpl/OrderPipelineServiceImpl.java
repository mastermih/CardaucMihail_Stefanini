package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderProductDaoImpl;
import com.ImperioElevator.ordermanagement.entity.AssignOrderResult;
import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class OrderPipelineServiceImpl {
    private final OrdersFunctionalServiceImpl ordersFunctionalService;
    private final OrderProductDaoImpl orderProductDaoImpl;

    @Autowired
    public OrderPipelineServiceImpl(OrderProductDaoImpl orderProductDaoImpl, OrdersFunctionalServiceImpl ordersFunctionalService) {
        this.ordersFunctionalService = ordersFunctionalService;
        this.orderProductDaoImpl = orderProductDaoImpl;
    }

    public final Function<Order, Long> orderPipeline  = order -> {
        Long orderId = ordersFunctionalService.createOrder.apply(order);
        List<OrderProduct> orderProducts = orderProductDaoImpl.findByOrderId(orderId);
        orderProducts.forEach(orderProduct -> ordersFunctionalService.processOrderProduct.accept(orderProduct, orderId));
        Notification notification = ordersFunctionalService.createNotification.apply(order);
        ordersFunctionalService.notifyUsers.accept(notification);
        return orderId;
    };

    public final Function<Order, Long> updateOrderStatusPipeline = order ->
            ordersFunctionalService.validateOrderProducts.andThen(ordersFunctionalService.emailConfirmation).apply(order);

    public final BiFunction<Order,String, String> assignOperatorToOrderPipeline  = (order, operator) -> {
        try {
            ordersFunctionalService.operatorAssigneeNotification.accept(order.orderId().id(), operator);
            return ordersFunctionalService.operatorAssignmentToOrder.apply(order.orderId().id(), operator);
        }catch (Exception e){
            throw new RuntimeException("Pipeline failed for assigning operator: " + e.getMessage(), e);
        }

    };

    public final BiFunction<Order, Long, Long> assineOrderToMePipeLine = (order, operatorId) -> {
        try {
            // Extract userId and result from updateOrderStatusAssigneOrderToMe
            AssignOrderResult result = ordersFunctionalService.updateOrderStatusAssigneOrderToMe.apply(order.orderId().id(), operatorId);

            Long userId = result.getUserId(); // Extract userId
            Long operationResult = result.getResult(); // Extract operation result

            // Create the notification for the order
            Notification notification = ordersFunctionalService.createNotificationAssineOrderToMe.apply(order);

            // Notify the users
            ordersFunctionalService.notifyUsersWithAssineOrderToMe.accept(order.orderId().id(), operatorId, userId);

            // Return the operation result (or notification ID)
            return notification.getNotificationId();
        } catch (Exception e) {
            throw new RuntimeException("Pipeline failed", e);
        }
    };

    };

}
