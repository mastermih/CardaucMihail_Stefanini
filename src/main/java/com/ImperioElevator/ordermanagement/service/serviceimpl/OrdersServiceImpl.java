package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.command.NotificationCommander;
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
import com.ImperioElevator.ordermanagement.valueobjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.Number;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.ImperioElevator.ordermanagement.enumobects.Status.READY_FOR_PAYMENT;

@Service
public class OrdersServiceImpl implements OrdersService {

   // private final OrderPipelineServiceImpl orderPipelineServiceImpl;
    private final OrderDaoImpl orderDao;
    private final OrderProductService orderProductService;
    private final OrderProductDaoImpl orderProductDaoImpl;
    private final ProductDaoImpl productDao;
    private final NotificationFactoryImpl notificationFactoryImpl;
    private final NotificationService notificationService;
    private final EmailServiceImpl emailService;
    private final NotificationCommander notificationCommander;
    private final InvoiceServiceImpl invoiceService;
    private final CdnService cdnService;

    private final EmailServiceFactory emailServiceFactory;
    private final UserDaoImpl userDao;

    private final Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);
    public OrdersServiceImpl(CdnService cdnService,InvoiceServiceImpl invoiceService,NotificationCommander notificationCommander, OrderDaoImpl orderDao, EmailServiceImpl emailService, NotificationService notificationService, OrderProductService orderProductService, OrderProductDaoImpl orderProductDaoImpl, ProductDaoImpl productDao, UserDaoImpl userDao, NotificationFactoryImpl notificationFactoryImpl, EmailServiceFactory emailServiceFactory) {
        this.orderDao = orderDao;
        this.orderProductService = orderProductService;
        this.orderProductDaoImpl = orderProductDaoImpl;
        this.productDao = productDao;
        this.userDao = userDao;
        this.notificationService = notificationService;
        this.notificationFactoryImpl = notificationFactoryImpl;
        this.emailServiceFactory = emailServiceFactory;
        this.emailService = emailService;
        this.notificationCommander = notificationCommander;
        this.invoiceService = invoiceService;
        this.cdnService = cdnService;
       // this.orderPipelineServiceImpl = orderPipelineServiceImpl;
    }
    @Value("${front.url}")
    private String frontUrl;

    @Override
    @Transactional //  atomicity
    public Long createOrderWithProducts(Order order, List<OrderProduct> orderProducts) throws SQLException {
        try {
            // Create the Order and get the generated orderId

            if (order == null || orderProducts == null || orderProducts.isEmpty()) {
                logger.error("Invalid input: Order is null or Order Products are null/empty");
                throw new IllegalArgumentException("Order and Order Products must not be null or empty");
            }

            Long orderId = orderDao.insert(order);
            logger.debug("Created Order with ID: {}", orderId);


            final Order finalOrder = new Order(
                    new Id(orderId),
                    order.userId(),
                    order.orderStatus(),
                    order.createdDate(),
                    order.updatedDate(),
                    order.orderInvoice(),
                    orderProducts
            );
            BiConsumer<OrderProduct, Order> orderProductConsumer = (orderProduct, orderInstance) -> {
                try {
                    OrderProduct updatedOrderProduct = new OrderProduct(
                            new Id(orderId),
                            orderInstance,
                            orderProduct.quantity(),
                            orderProduct.priceOrder(),
                            orderProduct.discount_percentages(), // Hardcoded discount percentage
                            orderProduct.price_discount(),              // Hardcoded price discount
                            orderProduct.VAT(),               // Hardcoded VAT
                            orderProduct.price_with_VAT(),
                            orderProduct.parentProductId(),
                            orderProduct.product()
                    );
                    // Insert the updated OrderProduct into the database
                    orderProductDaoImpl.insert(updatedOrderProduct);
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to process OrderProduct: " + e.getMessage(), e);
                }
            };

            //One of the problem is that if something fails here you will not know that happened you will get 401 user UNAUTHORIZED so you know
            String message = "New order has been created by the customer with ID " + finalOrder.userId().userId().id();
            Notification notification = notificationFactoryImpl.createOrderCreationNotification(message);
            // Save the notification to the database
            Long notificationId = notificationCommander.executeInAppNotification(notification);

            Consumer<User> userManagement = user -> {
                try {
                    UserNotification userNotification = notificationFactoryImpl.createUserNotificationOrderWithProducts(notificationId, user.userId().id());
                    notificationService.insertUserNotification(userNotification);// This is notifications for management users
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };

            List<User> usersManagement = userDao.getManagementUsers();
            usersManagement.forEach(userManagement);

            orderProducts.forEach(orderProduct -> orderProductConsumer.accept(orderProduct, finalOrder));
            return orderId;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //      public Long createOrderWithProducts(Order order, List<OrderProduct> orderProducts) throws SQLException {
//                    return orderPipelineServiceImpl.orderPipeline.apply(order);
//    }

    @Override
    public Paginable<OrdersFoundLastCreatedDTO> findPaginableOrderByCreatedDate(LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException {
        return orderDao.findPaginableOrderByCreatedDate(startDate, endDate, numberOfOrders, page);
    }

    @Override
    public Paginable<Order> findPaginableUserOrderByCreatedDate(Long id, LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException {
        return orderDao.findPaginableUserOrderByCreatedDate(id, startDate, endDate, numberOfOrders, page);
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
        AtomicBoolean priceChanged = new AtomicBoolean(false);

        // Check if the prices of any of the products have changed
        Predicate<OrderProduct> hasPriceChanged = orderProduct -> {
            try {
                Product product = productDao.findById(orderProduct.product().productId().id());
                if (product == null) {
                    throw new SQLException("Product not found for ID: " + orderProduct.product().productId().id());
                }
                boolean changed = !product.price().equals(orderProduct.priceOrder());
                if (changed) {
                    priceChanged.set(true);
                    orderProduct = new OrderProduct(
                            orderProduct.orderId(),
                            order,
                            orderProduct.quantity(),
                            product.price(),
                            orderProduct.discount_percentages(),
                            orderProduct.price_discount(),
                            orderProduct.VAT(),
                            orderProduct.price_with_VAT(),
                            orderProduct.parentProductId(),
                            orderProduct.product()
                    );
                    orderProductDaoImpl.update(orderProduct);
                }
                return changed;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };

        orderProducts.forEach(hasPriceChanged::test);

        if (!priceChanged.get()) {
            System.out.println("No price changes detected for order " + order.orderId().id());
        } else {
            System.out.println("Prices were updated for the order " + order.orderId().id());
        }

        // Retrieve the token for the order
        String token = orderDao.getTheConfirmationToken(order.orderId().id());
        if (token == null || token.isEmpty()) {
            throw new SQLException("No confirmation token found for order ID: " + order.orderId().id());
        }


        String confirmationLink = frontUrl + "sendMail/confirm/" + token;

       // String confirmationLink = "http://localhost:3000/sendMail/confirm/" + token;

        String message = "Your order with ID " + order.orderId().id() + " has been updated. Please confirm " + confirmationLink;


        // Create email details and send confirmation email
        EmailDetails emailDetails = emailServiceFactory.createEmailServiceUpdateOrderStatus(message);

        emailService.sendConfirmationMail(emailDetails, order.orderId().id());

        //  Update the order status CONFIRMED
        return orderDao.updateStatus(order);
    }
    //    public Long updateOrderStatus(Order order) throws SQLException {
//        return orderPipelineServiceImpl.updateOrderStatusPipeline(order);
//    }


    @Override
    public Long updateOrderStatusReadyForPayment(Long orderId, String jwtToken) throws SQLException {

        Order orderOperations = getOrderWithExtraProducts(orderId);
        orderDao.setOrderStatusToReadyPayment(orderOperations.orderId().id());
        // Reload the updated order to verify the status
        Order updatedOrder = getOrderWithExtraProducts(orderId);
        if(!READY_FOR_PAYMENT.equals(updatedOrder.orderStatus())){
            logger.error("Failed to set the order READY_FOR_PAYMENT " + (updatedOrder.orderStatus()));
            throw new RuntimeException("Order status update failed.");
        }

        invoiceService.prepareInvoiceForOrder(updatedOrder, jwtToken);

        //Below will be the notification for the management users

        return updatedOrder.orderId().id();
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

        Long notificationId = notificationCommander.executeInAppNotification(notification);


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
                order.orderInvoice(),
                order.orderProducts()
        );
        orderDao.updateStatus(updatedOrder);
        return orderDao.assigneeOperatorToOrder(id, name);
    }
//    public String assigneeOperatorToOrder(Long id, String name) throws SQLException {
//    return OrderPipelineServiceImpl.assignOperatorToOrderPipeline(Long id, String name);
//    }


    @Override
    public List<String> finedOperatorByName(String name) throws SQLException {
        return orderDao.finedOperatorByName(name);
    }

    @Override
    public List<String> getOperatorAssignedToOrder(Long orderId) throws SQLException {
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
                order.orderInvoice(),
                order.orderProducts()
        );
        Long userId = updatedOrder.userId().userId().id();
        orderDao.updateStatus(updatedOrder);

        String message = "The status of your order with Id " + orderId + " was updated to " + updatedOrder.orderStatus();

        Notification notification = notificationFactoryImpl.createOrderStatusUpdateNotification(message);

        Long notificationId = notificationCommander.executeInAppNotification(notification);


        UserNotification userNotification = notificationFactoryImpl.createUserNotificationAssineOrderToMe(notificationId, userId);

        notificationService.insertUserNotification(userNotification);
        return orderDao.assineOrderToMe(orderId, operatorId);
    }

    @Override
    public Long addOrderInvoice(Long orderId, String orderInvoice) throws SQLException {
        return orderDao.addOrderInvoice(orderId, orderInvoice);
    }

    @Override
    public Long setOrderStatusToReadyPayment(Long orderId) throws SQLException {
        return setOrderStatusToReadyPayment(orderId);
    }

//    public Long assineOrderToMe(Long orderId, Long operatorId) throws SQLException {
//        return OrderPipelineServiceImpl.assineOrderToMePipeLine(orderId, operatorId);
//    }
}