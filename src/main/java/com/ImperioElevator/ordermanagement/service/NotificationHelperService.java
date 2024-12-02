package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Order;
import org.springframework.core.io.ByteArrayResource;

import java.sql.SQLException;
import java.util.List;

public interface NotificationHelperService {
    Long insertNotificationWithInvoice(Order order, ByteArrayResource attachment, List<String> operators) throws SQLException;
}
