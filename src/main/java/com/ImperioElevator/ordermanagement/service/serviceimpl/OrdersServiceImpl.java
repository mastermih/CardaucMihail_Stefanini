package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderProductDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import com.ImperioElevator.ordermanagement.service.SaveOrderProductService;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrderDaoImpl orderDao;
    private final SaveOrderProductService saveOrderProductService;
    private final OrderProductDaoImpl orderProductDaoImpl;

    public OrdersServiceImpl(OrderDaoImpl orderDao, SaveOrderProductService saveOrderProductService, OrderProductDaoImpl orderProductDaoImple) {
        this.orderDao = orderDao;
        this.saveOrderProductService = saveOrderProductService;
        this.orderProductDaoImpl = orderProductDaoImple;
    }

    @Override
    @Transactional // Ensures atomicity
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
    public Paginable<Order> findPaginableOrderByCreatedDate(LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException {
        return orderDao.findPaginableOrderByCreatedDate(startDate, endDate, numberOfOrders, page);
    }

    @Override
    public Paginable<Order> findPaginableOrderByCreatedDateAndStatus(LocalDateTime startDate, LocalDateTime endDate, Status status, Long numberOfOrders, Long page) throws SQLException {
        return orderDao.findPaginableOrderByCreatedDateAndStatus(startDate, endDate, status, numberOfOrders, page);
    }

    @Override
    public List<Order> findLastCreatedOrders(Number limit) throws SQLException {
        return orderDao.findLastCreatedOrders(limit);
    }

    @Override
    public Long createOrder(Order order) throws SQLException {
        return orderDao.insert(order);
    }

    @Override
    public Long updateOrderStatus(Order order) throws SQLException {
        return orderDao.updateStatus(order);
    }

    @Override
    public Order fiendOrderById(Long id) throws SQLException {
        return orderDao.findById(id);
    }

    @Override
    public List<Object[]> getOrderWithExtraProducts(Long orderId) throws SQLException {
        return orderDao.getOrderWithExtraProducts(orderId);
    }


}