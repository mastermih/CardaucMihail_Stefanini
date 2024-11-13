package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderProductDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;
import com.ImperioElevator.ordermanagement.dto.OrdersFoundLastCreatedDTO;
import com.ImperioElevator.ordermanagement.entity.*;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.factory.EmailServiceFactory;
import com.ImperioElevator.ordermanagement.factory.NotifiactionFactory;
import com.ImperioElevator.ordermanagement.factory.factoryimpl.NotificationFactoryImpl;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.NotificationService;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import com.ImperioElevator.ordermanagement.service.OrderProductService;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.UpdateDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrderDaoImpl orderDao;
    private final OrderProductService orderProductService;
    private final OrderProductDaoImpl orderProductDaoImpl;
    private final ProductDaoImpl productDao;
    private final NotificationFactoryImpl notificationFactoryImpl;
    private final NotificationService notificationService;
    private final EmailServiceImpl emailService;

    private final EmailServiceFactory emailServiceFactory;
    private final UserDaoImpl userDao;
    public OrdersServiceImpl(OrderDaoImpl orderDao, EmailServiceImpl emailService ,NotificationService notificationService, OrderProductService orderProductService, OrderProductDaoImpl orderProductDaoImpl, ProductDaoImpl productDao, UserDaoImpl userDao, NotificationFactoryImpl notificationFactoryImpl, EmailServiceFactory emailServiceFactory) {
        this.orderDao = orderDao;
        this.orderProductService = orderProductService;
        this.orderProductDaoImpl = orderProductDaoImpl;
        this.productDao = productDao;
        this.userDao = userDao;
        this.notificationService = notificationService;
        this.notificationFactoryImpl = notificationFactoryImpl;
        this.emailServiceFactory = emailServiceFactory;
        this.emailService = emailService;
    }

    @Override
    @Transactional //  atomicity
    public Long createOrderWithProducts(Order order, List<OrderProduct> orderProducts) throws SQLException {
        // Create the Order and get the generated orderId
        Long orderId = orderDao.insert(order);
        order = new Order(new Id(orderId), order.userId(), order.orderStatus(), order.createdDate(), order.updatedDate(), orderProducts);
        //  Create OrderProduct entities linked to  order
        for (OrderProduct orderProduct : orderProducts) {
            // Update OrderProduct with the new orderId
            OrderProduct updatedOrderProduct = new OrderProduct(
                    new Id(orderId),
                    order,
                    orderProduct.quantity(),
                    orderProduct.priceOrder(),
                    orderProduct.parentProductId(),
                    orderProduct.product()
            );
            //ToDO the notification have to be safe when the order is created add it in the return or insert also I think I can set the message somewhere else
            //One of the problem is that if something fails here you will not know that happened you will get 401 user UNAUTHORIZED so you know
            String message = "New order has been created by the customer with ID " + order.userId().userId().id();
            Notification notification = notificationFactoryImpl.createOrderCreationNotification(message);
            // Save the notification to the database
            Long notificationId = notificationService.insert(notification);
            List<User> userManagement = userDao.getManagementUsers();
            for (User user : userManagement) {
                // Create a UserNotification entry for each management user // This parameters are extra because we add them in the factory method
                UserNotification userNotification = notificationFactoryImpl.createUserNotificationOrderWithProducts(notificationId, user.userId().id());

                notificationService.insertUserNotification(userNotification);
            }
            orderProductDaoImpl.insert(updatedOrderProduct);
        }

        return orderId;
    }

    @Override
    public  Paginable<OrdersFoundLastCreatedDTO> findPaginableOrderByCreatedDate(LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException {
        return orderDao.findPaginableOrderByCreatedDate(startDate, endDate, numberOfOrders, page);
    }

    @Override
    public Paginable<Order> findPaginableUserOrderByCreatedDate(Long id, LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException {
        return orderDao.findPaginableUserOrderByCreatedDate(id,startDate,endDate,numberOfOrders,page);
    }

    @Override
    public Paginable<OrdersFoundLastCreatedDTO> findPaginableOrderByCreatedDateAndStatus(LocalDateTime startDate, LocalDateTime endDate, Status status, Long numberOfOrders, Long page) throws SQLException {
        return orderDao.findPaginableOrderByCreatedDateAndStatus(startDate, endDate, status, numberOfOrders, page);
    }

    @Override
    public List<OrdersFoundLastCreatedDTO> findLastCreatedOrders(Number limit) throws SQLException {
        return orderDao.findLastCreatedOrders(limit);
    }

    @Override
    public List<Order> findLastCreatedOrdersForUserRole(Number limit, Long id) throws SQLException {
        return orderDao.findLastCreatedOrdersForUserRole(limit, id);
    }

    @Override
    public Long createOrder(Order order) throws SQLException {
        return orderDao.insert(order);
    }

    @Override
    public Long updateOrderStatus(Order order) throws SQLException {
        // Retrieve order products associated with the order
        List<OrderProduct> orderProducts = orderProductDaoImpl.findByOrderId(order.orderId().id());
        boolean priceChanged = false;

        // Check if the prices of any of the products have changed
        for (OrderProduct orderProduct : orderProducts) {
            Product product = productDao.findById(orderProduct.product().productId().id());
            if (product == null) {
                throw new SQLException("Product not found for ID: " + orderProduct.product().productId().id());
            }

            if (!product.price().equals(orderProduct.priceOrder())) {
                priceChanged = true;

                // Update the product price in the orderProduct
                orderProduct = new OrderProduct(
                        orderProduct.orderId(),
                        order,
                        orderProduct.quantity(),
                        product.price(),
                        orderProduct.parentProductId(),
                        orderProduct.product()
                );

                // Update the orderProduct with new price
                orderProductDaoImpl.update(orderProduct);
            }
        }

        //  Send confirmation email with token if necessary
        if (!priceChanged) {
            System.out.println("No price changes detected for order " + order.orderId().id());
        } else {
            System.out.println("Prices were updated for the order " + order.orderId().id());
        }

        // Retrieve the token for the order
        String token = orderDao.getTheConfirmationToken(order.orderId().id());
        if (token == null || token.isEmpty()) {
            throw new SQLException("No confirmation token found for order ID: " + order.orderId().id());
        }


       // String message = "Your order with ID " + order.orderId().id() + " has been updated. Please confirm " + conf;
        String confirmationLink = "http://localhost:3000/sendMail/confirm/" + token;

        String message = "Your order with ID " + order.orderId().id() + " has been updated. Please confirm " + confirmationLink;


        // Create email details and send confirmation email
        EmailDetails emailDetails = emailServiceFactory.createEmailServiceUpdateOrderStatus(message);

        emailService.sendConfirmationMail(emailDetails, order.orderId().id());

        //  Update the order status CONFIRMED
        return orderDao.updateStatus(order);
    }

    @Override
    public Order fiendOrderById(Long id) throws SQLException {
        return orderDao.findById(id);
    }

    @Override
    public Order getOrderWithExtraProducts(Long orderId) throws SQLException {
        return orderDao.getOrderWithExtraProducts(orderId);
    }

    @Override
    public String assigneeOperatorToOrder(Long id, String name) throws SQLException {
        String message = "New order has been assigned to the operator with name " + name;

        Notification notification = notificationFactoryImpl.createOrderAssignmentNotification(message);

        Long notificationId =  notificationService.insert(notification);

        Long userId = userDao.findUserIdByName(name); // This is a 'castili' so I had no idea how to get the user id

        UserNotification userNotification = notificationFactoryImpl.createUserNotificationAssigneeOperatorToOrder(notificationId, userId);

        notificationService.insertUserNotification(userNotification);

        Order order = orderDao.findById(id);//id is the oder id
        Order updatedOrder = new Order(
                order.orderId(),
                order.userId(),
                Status.IN_PROGRESS,
                order.createdDate(),
                new UpdateDateTime(LocalDateTime.now()),
                order.orderProducts()
        );
        orderDao.updateStatus(updatedOrder);
        return orderDao.assigneeOperatorToOrder(id, name);
    }

    @Override
    public List<String> finedOperatorByName(String name) throws SQLException {
        return orderDao.finedOperatorByName(name);
    }

    @Override
    public  List<String>  getOperatorAssignedToOrder(Long orderId) throws SQLException {
        return orderDao.getOperatorAssignedToOrder(orderId);
    }

    @Override
    public Long deleteOperatorAssignedToOrderByOperatorId(Long orderId, Long operatorId) throws SQLException {
        return orderDao.deleteOperatorAssignedToOrderByOperatorId(orderId, operatorId);
    }

    @Override
    public Long deleteAllOperatorsAssignedToOrderByOperatorId(Long orderId) throws SQLException {
        return orderDao.deleteAllOperatorsAssignedToOrderByOperatorId(orderId);
    }

    @Override
    public Long assineOrderToMe(Long orderId, Long operatorId) throws SQLException {
        Order order = orderDao.findById(orderId);
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

                String message = "The status of your order with Id " + orderId + " was updated to " + updatedOrder.orderStatus();

                Notification notification = notificationFactoryImpl.createOrderStatusUpdateNotification(message);
                Long notificationId = notificationService.insert(notification);

                UserNotification userNotification =  notificationFactoryImpl.createUserNotificationAssineOrderToMe(notificationId, userId);

                notificationService.insertUserNotification(userNotification);
        return orderDao.assineOrderToMe(orderId, operatorId);
    }

}