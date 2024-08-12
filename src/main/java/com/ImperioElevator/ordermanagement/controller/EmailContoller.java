package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping()
public class EmailContoller {


private final EmailService emailService;

@Autowired
    public EmailContoller(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendMail")
    public String sendMail(@RequestBody EmailDetails details) {
        return emailService.sendSimpleMail(details);
    }
    @PostMapping("/sendMail/OrderId")
    public String sendMailOrderId(@RequestBody EmailDetails details, Long orderId) {
        return emailService.sendConfirmationMail(details, orderId);
    }
    @PostMapping("/sendMail/confirm/{id}")
     public Long sendConfirmationOrderEmailStatus(@RequestBody Order order) throws SQLException {
    return emailService.updateOrderEmailConfirmStatus(order);
    }
    @GetMapping("/sendMail/confirm/{id}")
         public Order getOrderIdConfirmEmail(@PathVariable("id") Long id) throws SQLException{
         return null;
    }
}

