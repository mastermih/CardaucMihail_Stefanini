//package com.ImperioElevator.ordermanagement.service.serviceimpl;
//
//import com.ImperioElevator.ordermanagement.command.NotificationCommander;
//import com.ImperioElevator.ordermanagement.dao.UserDao;
//import com.ImperioElevator.ordermanagement.dao.daoimpl.*;
//import com.ImperioElevator.ordermanagement.entity.*;
//import com.ImperioElevator.ordermanagement.enumobects.Status;
//import com.ImperioElevator.ordermanagement.factory.EmailServiceFactory;
//import com.ImperioElevator.ordermanagement.factory.factoryimpl.NotificationFactoryImpl;
//import com.ImperioElevator.ordermanagement.service.EmailService;
//import com.ImperioElevator.ordermanagement.service.NotificationService;
//import com.ImperioElevator.ordermanagement.valueobjects.Id;
//import com.ImperioElevator.ordermanagement.valueobjects.UpdateDateTime;
//import org.apache.logging.log4j.util.TriConsumer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cglib.proxy.Factory;
//import org.springframework.stereotype.Service;
//
//import java.rmi.MarshalledObject;
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.function.*;
//
//@Service
//public class OrdersFunctionalServiceImpl {
//
//    private final OrderDaoImpl orderDao;
//    private final OrderProductDaoImpl orderProductDaoImpl;
//    private final NotificationCommander notificationCommander;
//    private final UserDaoImpl userDao;
//    private final NotificationFactoryImpl notificationFactoryImpl;
//    private final ProductDaoImpl productDao;
//    private final EmailServiceFactory emailServiceFactory;
//    private final EmailService emailService;
//    private final NotificationService notificationService;
//
//    @Autowired
//    public OrdersFunctionalServiceImpl(NotificationService notificationService, OrderDaoImpl orderDao, OrderProductDaoImpl orderProductDaoImpl,
//                                       NotificationCommander notificationCommander, UserDaoImpl userDao,
//                                       NotificationFactoryImpl notificationFactoryImpl, ProductDaoImpl productDao,
//                                       EmailServiceFactory emailServiceFactory, EmailService emailService) {
//        this.notificationService = notificationService;
//        this.orderDao = orderDao;
//        this.orderProductDaoImpl = orderProductDaoImpl;
//        this.notificationCommander = notificationCommander;
//        this.userDao = userDao;
//        this.notificationFactoryImpl = notificationFactoryImpl;
//        this.productDao = productDao;
//        this.emailServiceFactory = emailServiceFactory;
//        this.emailService = emailService;
//    }
////ToDo add a biFunction so it do not see the orderDao
//        public final Function<Order, Long> createOrder = orderToCreate -> {
//            try {
//                return orderDao.insert(orderToCreate);
//            } catch (SQLException e) {
//                throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
//            }
//        };
//
//        public final BiConsumer<OrderProduct, Long> processOrderProduct = (orderProduct, orderId) -> {
//            try {
//                OrderProduct updatedOrderProduct = new OrderProduct(
//                        new Id(orderId), //Not sure that this will work fine
//                        null,  // this is just a place holder
//                        orderProduct.quantity(),
//                        orderProduct.priceOrder(),
//                        orderProduct.parentProductId(),
//                        orderProduct.product()
//                );
//                orderProductDaoImpl.insert(updatedOrderProduct);
//            } catch (SQLException e) {
//                throw new RuntimeException("Failed to process order product: " + e.getMessage(), e);
//            }
//        };
//
//       public final Function<Order, Notification> createNotification = orderToNotify -> notificationFactoryImpl.createOrderCreationNotification(
//                "New order created by customer ID: " + orderToNotify.userId().userId().id()
//        );
//
//       public final Consumer<Notification> notifyUsers = notification -> {
//            try {
//                Long notificationId = notificationCommander.executeInAppNotification(notification);
//                userDao.getManagementUsers().forEach(user -> {
//                    try {
//                        UserNotification userNotification = notificationFactoryImpl.createUserNotificationOrderWithProducts(
//                                notificationId, user.userId().id()
//                        );
//                        notificationService.insertUserNotification(userNotification);
//                    } catch (SQLException e) {
//                        throw new RuntimeException("Failed to notify user: " + e.getMessage(), e);
//                    }
//                });
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        };
//
//     Predicate<OrderProduct> hasPriceChanged = orderProduct -> {
//        try {
//            // Fetch the product from the database
//            Product product = productDao.findProductId(orderProduct.product().productId().id());
//            if(product == null){
//                throw new RuntimeException("Product not found for Id " + orderProduct.orderId().id());
//            }
//            // Check if the price has changed
//            if (!product.price().equals(orderProduct.priceOrder().price())) {
//                throw new RuntimeException("Product price has changed. The confirmaton failed ");
//            }
//            return false;
//        }catch (SQLException e){
//            throw new RuntimeException("Failed to check if price has changed " + e);
//        }
//     };
//
//    public final Function<Order, Order> validateOrderProducts = order -> {
//        List<OrderProduct> orderProducts = orderProductDaoImpl.findByOrderId(order.orderId().id());
//        for(OrderProduct orderProduct:orderProducts){
//            hasPriceChanged.test(orderProduct);
//            }
//        return order;
//        };
//
//    Function<Order, Long> emailConfirmation = order -> {
//        try{
//            String token = orderDao.getTheConfirmationToken(order.orderId().id());
//            if (token == null || token.isEmpty()) {
//                throw new SQLException("No confirmation token found for order ID: " + order.orderId().id());
//            }
//            String confirmationLink = "http://localhost:3000/sendMail/confirm/" + token;
//            String message = "Your order with ID " + order.orderId().id() + " has been updated. Please confirm " + confirmationLink;
//            EmailDetails emailDetails = emailServiceFactory.createEmailServiceUpdateOrderStatus(message);
//            emailService.sendConfirmationMail(emailDetails, order.orderId().id());
//            return orderDao.updateStatus(order);
//        }catch (SQLException e){
//            throw new RuntimeException("Failed to create the email " + e);
//        }
//    };
//
//    //toDo I think we can create man interface for the notification
//     BiConsumer<Long, String> operatorAssigneeNotification = (orderId, operatorName) -> {
//        try {
//            Notification notification = notificationFactoryImpl.createOrderAssignmentNotification("New order has been assigned to the operator with name " + operatorName);
//            Long notificationId = notificationCommander.executeInAppNotification(notification);
//            Long operatorId = userDao.findUserIdByName(operatorName);
//            if(operatorId == null){
//                throw new RuntimeException("Failed to get the operatorId " + operatorId);
//            }
//            UserNotification userNotification = notificationFactoryImpl.createUserNotificationAssigneeOperatorToOrder(
//                    notificationId, operatorId
//            );
//            notificationService.insertUserNotification(userNotification);
//        }catch (SQLException e){
//            throw  new RuntimeException("Failed to send notification for operator assign " + e);
//        }
//     };
//
//    //I think it have to be operator id do not forget to check
//    public final BiFunction<Long, String, String> operatorAssignmentToOrder = (orderId, operatorName) -> {
//        try{
//            Order order = orderDao.findById(orderId);//id is the oder id
//
//            if (order == null) {
//                throw new RuntimeException("Order not found for ID: " + orderId);
//            }
//
//            Order updatedOrder = new Order(
//                    order.orderId(),
//                    order.userId(),
//                    Status.IN_PROGRESS,
//                    order.createdDate(),
//                    new UpdateDateTime(LocalDateTime.now()),
//                    order.orderProducts()
//            );
//            orderDao.updateStatus(updatedOrder);
//            return orderDao.assigneeOperatorToOrder(orderId, operatorName);
//        }catch (SQLException e){
//            throw new RuntimeException("Failed to assigne the operator to the order " + e);
//        }
//    };
//
//    // One last method left assineOrderToMe
//
//    //I think it have to be operator id do not forget to check
//
//    public final BiFunction<Long,Long, AssignOrderResult> updateOrderStatusAssigneOrderToMe = (orderId, operatorId) -> {
//        try {
//            Order order = orderDao.findById(orderId);
//            if (order == null) {
//                throw new RuntimeException("Order not found for ID: " + orderId);
//            }
//            Order updatedOrder = new Order(
//                    order.orderId(),
//                    order.userId(),
//                    Status.IN_PROGRESS,
//                    order.createdDate(),
//                    new UpdateDateTime(LocalDateTime.now()),
//                    order.orderProducts()
//            );
//            Long userId = updatedOrder.userId().userId().id();
//            orderDao.updateStatus(updatedOrder);
//            Long result  = orderDao.assineOrderToMe(orderId, operatorId);
//            //DO not forget to fix that
//            Map<Long,Long> entry1  = (Map<Long, Long>) Map.entry(userId, result);
//            return new AssignOrderResult(userId, result);
//        }catch (SQLException e){
//        throw new RuntimeException("Failed to updateOrderStatusAssigneOrderToMe " + e);
//        }
//    };
//
//    public final Function<Order, Notification> createNotificationAssineOrderToMe = orderToNotify -> notificationFactoryImpl.createOrderCreationNotification(
//            "The status of your order with Id " + orderToNotify.orderId().id() + " was updated to " + orderToNotify.orderStatus()
//    );
//
//
//    //ToDO to I think I messed the update fo the order in my functions after I add the operator
//    public final TriConsumer<Long, Long, Long> notifyUsersWithAssineOrderToMe = (orderId, operatorId, userId) -> {
//        try {
//            Long notificationId = notificationCommander.executeInAppNotification(orderId);
//            UserNotification userNotification =  notificationFactoryImpl.createUserNotificationAssineOrderToMe(notificationId, userId);
//            notificationService.insertUserNotification(userNotification);
//            return orderDao.assineOrderToMe(orderId, operatorId);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    };
//
//    }
//
