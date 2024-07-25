package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {


    private final OrderDaoImpl orderDao;

    public OrdersServiceImpl(OrderDaoImpl orderDao) {
        this.orderDao = orderDao;
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

}