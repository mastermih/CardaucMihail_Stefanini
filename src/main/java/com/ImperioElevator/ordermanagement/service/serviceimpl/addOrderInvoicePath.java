package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Order;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.function.Consumer;
@Service
public class addOrderInvoicePath {
   private final OrderDaoImpl orderDao;

    public addOrderInvoicePath(OrderDaoImpl orderDao) {
        this.orderDao = orderDao;
    }


    public void processInvoice(Order order, String invoiceFullPath) {
        Consumer<Order> updateOrderWithInvoice = o -> {
            try {
                orderDao.addOrderInvoice(o.orderId().id(), invoiceFullPath);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };

        updateOrderWithInvoice.accept(order);
    }

}
