package com.ImperioElevator.ordermanagement.service;


import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import org.springframework.core.io.ByteArrayResource;

import java.sql.SQLException;

public interface EmailService {

  //  String sendSimpleMail(EmailDetails details);
    String sendConfirmationMail(EmailDetails details, Long token);
    String updateOrderEmailConfirmStatus(String token) throws SQLException;

    String updateUserEmailConfirmStatus(String token) throws SQLException;
    //File attachments have to be provided to this email
    String sendInvoiceEmail(EmailDetails details, ByteArrayResource byteArrayResource) throws SQLException;
}
