package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.command.NotificationCommander;
import com.ImperioElevator.ordermanagement.dao.UserDao;
import com.ImperioElevator.ordermanagement.dao.daoimpl.*;
import com.ImperioElevator.ordermanagement.entity.*;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.factory.EmailServiceFactory;
import com.ImperioElevator.ordermanagement.factory.factoryimpl.NotificationFactoryImpl;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.NotificationService;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.UpdateDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Factory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;

@Service
public class OrdersFunctionalServiceImpl {

    private final OrderDaoImpl orderDao;
    private final OrderProductDaoImpl orderProductDaoImpl;
    private final NotificationCommander notificationCommander;
    private final UserDaoImpl userDao;
    private final NotificationFactoryImpl notificationFactoryImpl;
    private final ProductDaoImpl productDao;
    private final EmailServiceFactory emailServiceFactory;
    private final EmailService emailService;
    private final NotificationService notificationService;

    @Autowired
    public OrdersFunctionalServiceImpl(NotificationService notificationService, OrderDaoImpl orderDao, OrderProductDaoImpl orderProductDaoImpl,
                                       NotificationCommander notificationCommander, UserDaoImpl userDao,
                                       NotificationFactoryImpl notificationFactoryImpl, ProductDaoImpl productDao,
                                       EmailServiceFactory emailServiceFactory, EmailService emailService) {
        this.notificationService = notificationService;
        this.orderDao = orderDao;
        this.orderProductDaoImpl = orderProductDaoImpl;
        this.notificationCommander = notificationCommander;
        this.userDao = userDao;
        this.notificationFactoryImpl = notificationFactoryImpl;
        this.productDao = productDao;
        this.emailServiceFactory = emailServiceFactory;
        this.emailService = emailService;
    }

        public final Function<Order, Long> createOrder = orderToCreate -> {
            try {
                return orderDao.insert(orderToCreate);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
            }
        };

        public final BiConsumer<OrderProduct, Long> processOrderProduct = (orderProduct, orderId) -> {
            try {
                OrderProduct updatedOrderProduct = new OrderProduct(
                        new Id(orderId), //Not sure that this will work fine
                        null,  // this is just a place holder
                        orderProduct.quantity(),
                        orderProduct.priceOrder(),
                        orderProduct.parentProductId(),
                        orderProduct.product()
                );
                orderProductDaoImpl.insert(updatedOrderProduct);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to process order product: " + e.getMessage(), e);
            }
        };

       public final Function<Order, Notification> createNotification = orderToNotify -> notificationFactoryImpl.createOrderCreationNotification(
                "New order created by customer ID: " + orderToNotify.userId().userId().id()
        );

       public final Consumer<Notification> notifyUsers = notification -> {
            try {
                Long notificationId = notificationCommander.executeInAppNotification(notification);
                userDao.getManagementUsers().forEach(user -> {
                    try {
                        UserNotification userNotification = notificationFactoryImpl.createUserNotificationOrderWithProducts(
                                notificationId, user.userId().id()
                        );
                        notificationService.insertUserNotification(userNotification);
                    } catch (SQLException e) {
                        throw new RuntimeException("Failed to notify user: " + e.getMessage(), e);
                    }
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };

       public final Function<Order, Long> orderPipeline  = order -> {
           Long orderId = createOrder.apply(order);
           List<OrderProduct> orderProducts = orderProductDaoImpl.findByOrderId(orderId);
           orderProducts.forEach(orderProduct -> processOrderProduct.accept(orderProduct, orderId));
           Notification notification = createNotification.apply(order);
           notifyUsers.accept(notification);
           return orderId;
       };

    public Long createOrderWithProducts(Order order) {
        // pipeline
        return orderPipeline.apply(order);
    }

//

     Predicate<OrderProduct> hasPriceChanged = orderProduct -> {
        try {
            // Fetch the product from the database
            Product product = productDao.findProductId(orderProduct.product().productId().id());
            if(product == null){
                throw new RuntimeException("Product not found for Id " + orderProduct.orderId().id());
            }
            // Check if the price has changed
            if (!product.price().equals(orderProduct.priceOrder().price())) {
                throw new RuntimeException("Product price has changed. The confirmaton failed ");
            }
            return false;
        }catch (SQLException e){
            throw new RuntimeException("Failed to check if price has changed " + e);
        }
     };

    public final Function<Order, Order> validateOrderProducts = order -> {
        List<OrderProduct> orderProducts = orderProductDaoImpl.findByOrderId(order.orderId().id());
        for(OrderProduct orderProduct:orderProducts){
            hasPriceChanged.test(orderProduct);
            }
        return order;
        };

    Function<Order, Long> emailConfirmation = order -> {
        try{
            String token = orderDao.getTheConfirmationToken(order.orderId().id());
            if (token == null || token.isEmpty()) {
                throw new SQLException("No confirmation token found for order ID: " + order.orderId().id());
            }
            String confirmationLink = "http://localhost:3000/sendMail/confirm/" + token;
            String message = "Your order with ID " + order.orderId().id() + " has been updated. Please confirm " + confirmationLink;
            EmailDetails emailDetails = emailServiceFactory.createEmailServiceUpdateOrderStatus(message);
            emailService.sendConfirmationMail(emailDetails, order.orderId().id());
            return orderDao.updateStatus(order);
        }catch (SQLException e){
            throw new RuntimeException("Failed to create the email " + e);
        }
    };

    public final Function<Order, Long> updateOrderStatusPipeline = order ->
            validateOrderProducts.andThen(emailConfirmation).apply(order);

   //Controller
    public Long updateOrderStatus(Order order){
      return updateOrderStatusPipeline.apply(order);
    }

     BiConsumer<Long, String> operatorAssigneeNotification = (orderId, operatorName) -> {
        try {
            Notification notification = notificationFactoryImpl.createOrderAssignmentNotification("New order has been assigned to the operator with name " + operatorName);
            Long notificationId = notificationCommander.executeInAppNotification(notification);
            Long operatorId = userDao.findUserIdByName(operatorName);
            if(operatorId == null){
                throw new RuntimeException("Failed to get the operatorId " + operatorId);
            }
            UserNotification userNotification = notificationFactoryImpl.createUserNotificationAssigneeOperatorToOrder(
                    notificationId, operatorId
            );
            notificationService.insertUserNotification(userNotification);
        }catch (SQLException e){
            throw  new RuntimeException("Failed to send notification for operator assign " + e);
        }
     };

    //I think it have to be operator id do not forget to check
    public final BiFunction<Long, String, String> operatorAssignmentToOrder = (orderId, operatorName) -> {
        try{
            Order order = orderDao.findById(orderId);//id is the oder id

            if (order == null) {
                throw new RuntimeException("Order not found for ID: " + orderId);
            }

            Order updatedOrder = new Order(
                    order.orderId(),
                    order.userId(),
                    Status.IN_PROGRESS,
                    order.createdDate(),
                    new UpdateDateTime(LocalDateTime.now()),
                    order.orderProducts()
            );
            orderDao.updateStatus(updatedOrder);
            return orderDao.assigneeOperatorToOrder(orderId, operatorName);
        }catch (SQLException e){
            throw new RuntimeException("Failed to assigne the operator to the order " + e);
        }
    };

    BiFunction<Order,String, String> assignOperatorToOrderPipeline  = (order,operator) -> {
        try {
            operatorAssigneeNotification.accept(order.orderId().id(), operator);
            return operatorAssignmentToOrder.apply(order.orderId().id(), operator);
        }catch (Exception e){
            throw new RuntimeException("Pipeline failed for assigning operator: " + e.getMessage(), e);
        }

    };
    //Check once again
    public String assigneeOperatorToOrder(Order order, String operatorName) {
        return assignOperatorToOrderPipeline.apply(order, operatorName);
    }

    // One last method left assineOrderToMe

    //I think it have to be operator id do not forget to check

    BiFunction<Long,Long, Long> updateOrderStatusAssigneOrderToMe = (orderId, operatorId) -> {
        try {
            Order order = orderDao.findById(orderId);
            if (order == null) {
                throw new RuntimeException("Order not found for ID: " + orderId);
            }
            Order updatedOrder = new Order(
                    order.orderId(),
                    order.userId(),
                    Status.IN_PROGRESS,
                    order.createdDate(),
                    new UpdateDateTime(LocalDateTime.now()),
                    order.orderProducts()
            );
            Long userId = updatedOrder.userId().userId().id();
            orderDao.updateStatus(updatedOrder);
            return orderDao.assineOrderToMe(orderId, operatorId);
        }catch (SQLException e){
        throw new RuntimeException("Failed to updateOrderStatusAssigneOrderToMe " + e);
        }
    };

    public final Function<Order, Notification> createNotificationAssineOrderToMe = orderToNotify -> notificationFactoryImpl.createOrderCreationNotification(
            "The status of your order with Id " + orderToNotify.orderId().id() + " was updated to " + orderToNotify.orderStatus()
    );

    public final BiConsumer<Notification, Long> notifyUsersWithAssineOrderToMe = (orderId, operatorId) -> {
        try {
            Long notificationId = notificationCommander.executeInAppNotification(orderId);
            UserNotification userNotification =  notificationFactoryImpl.createUserNotificationAssineOrderToMe(notificationId, userId);

            notificationService.insertUserNotification(userNotification);
            return orderDao.assineOrderToMe(orderId, operatorId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    BiFunction<Order, Long, Long>assineOrderToMePipeLine = (order,operatorId) -> {
        try {
            updateOrderStatusAssigneOrderToMe.apply(order.orderId().id(), operatorId);
            return createNotificationAssineOrderToMe.apply(order).getNotificationId();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    };

    public Long assineOrderToMe(Order order, Long operatorId) throws SQLException {
        assineOrderToMePipeLine.apply(order,operatorId);
        return order.orderId().id();
    };
    }

