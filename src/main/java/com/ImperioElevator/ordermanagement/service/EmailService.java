package com.ImperioElevator.ordermanagement.service;


import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.entity.Order;

import java.sql.SQLException;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);
    String sendConfirmationMail(EmailDetails details, Long orderId);
    Long updateOrderEmailConfirmStatus(Order order) throws SQLException;
}
