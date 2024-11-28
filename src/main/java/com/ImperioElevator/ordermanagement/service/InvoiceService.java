package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Order;
import org.springframework.core.io.ByteArrayResource;

import java.sql.SQLException;

public interface InvoiceService {

        ByteArrayResource createOrderInvoice(Order order);
        void handleInvoiceForOrder(Order order, String jwtToken) throws SQLException;
}