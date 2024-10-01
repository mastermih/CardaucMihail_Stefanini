package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderProductDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.dto.OrdersFoundLastCreatedDTO;
import com.ImperioElevator.ordermanagement.entity.*;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import com.ImperioElevator.ordermanagement.service.OrderProductService;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
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
    private final EmailServiceImpl emailService;

    public OrdersServiceImpl(OrderDaoImpl orderDao, OrderProductService orderProductService, OrderProductDaoImpl orderProductDaoImpl, ProductDaoImpl productDao, EmailServiceImpl emailService) {
        this.orderDao = orderDao;
        this.orderProductService = orderProductService;
        this.orderProductDaoImpl = orderProductDaoImpl;
        this.productDao = productDao;
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
        // Step 1 Retrieve order products associated with the order
        List<OrderProduct> orderProducts = orderProductDaoImpl.findByOrderId(order.orderId().id());
        boolean priceChanged = false;

        // Step 2 Check if the prices of any of the products have changed
        for (OrderProduct orderProduct : orderProducts) {
            Product product = productDao.findById(orderProduct.product().productId().id());
            if (product == null) {
                throw new SQLException("Product not found for ID: " + orderProduct.product().productId().id());
            }

            if (!product.price().equals(orderProduct.priceOrder())) {
                priceChanged = true;

                // Step 3 Update the product price in the orderProduct
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

        // Step 4 Send confirmation email with token if necessary
        if (!priceChanged) {
            System.out.println("No price changes detected for order " + order.orderId().id());
        } else {
            System.out.println("Prices were updated for the order " + order.orderId().id());
        }

        // Step 5 Retrieve the token for the order
        String token = orderDao.getTheConfirmationToken(order.orderId().id());
        if (token == null || token.isEmpty()) {
            throw new SQLException("No confirmation token found for order ID: " + order.orderId().id());
        }

        // Step 6 Construct and send confirmation email
        EmailDetails emailDetails = constructEmailDetails(order, token);
        String emailResult = emailService.sendConfirmationMail(emailDetails, order.orderId().id());
        System.out.println("Email Result: " + emailResult);

        // Step 7 Update the order status CONFIRMED
        return orderDao.updateStatus(order);
    }

    private EmailDetails constructEmailDetails(Order order, String token) {
        // Construct email details based on the order information
        String recipient = "cardaucmihai@gmail.com";
        String subject = "Order Confirmation";
        String confirmationLink = "http://localhost:3000/sendMail/confirm/" + token;
        String messageBody = "Your order with ID " + order.orderId().id() + " has been successfully created.\n\n"
                + "Please confirm your order by clicking the link below:\n"
                + confirmationLink;
        EmailDetails details = new EmailDetails();
        details.setRecipient(recipient);
        details.setSubject(subject);
        details.setMsgBody(messageBody);
        details.setOrderId(order.orderId().id()); // Ensure orderId is included
        return details;
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
        return orderDao.assineOrderToMe(orderId, operatorId);
    }

}