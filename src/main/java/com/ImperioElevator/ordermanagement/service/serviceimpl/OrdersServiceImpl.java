package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class OrdersServiceImpl implements OrdersService {
    private final OrderDaoImpl orderDao;

    @Autowired
    public OrdersServiceImpl(OrderDaoImpl orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Paginable<Order> findPaginableOrderByCreatedDate(LocalDateTime createdDate, Long numberOfOrders, Long page) throws SQLException {
        return orderDao.findPaginableOrderByCreatedDate(createdDate, numberOfOrders, page);
    }

    @Override
    public Paginable<Order> findPaginableOrderByCreatedDateAndStatus(LocalDateTime createdDate, Status status, Long numberOfOrders, Long page) throws SQLException {
        return orderDao.findPaginableOrderByCreatedDateAndStatus(createdDate, status, numberOfOrders, page);
    }
}
