package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Order;
import org.springframework.core.io.ByteArrayResource;

import java.sql.SQLException;
import java.util.List;

public interface InvoiceService {

        ByteArrayResource createOrderInvoice(Order order);
        void prepareInvoiceForOrder(Order order, String jwtToken, List<String> operators) throws SQLException;
}