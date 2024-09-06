package com.ImperioElevator.ordermanagement.service;


import com.ImperioElevator.ordermanagement.entity.EmailDetails;

import java.sql.SQLException;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);
    String sendConfirmationMail(EmailDetails details, Long token);
    String updateOrderEmailConfirmStatus(String token) throws SQLException;

    String updateUserEmailConfirmStatus(String token) throws SQLException;
}
