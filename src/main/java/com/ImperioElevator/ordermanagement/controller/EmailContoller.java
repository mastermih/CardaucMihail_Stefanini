package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class EmailContoller {


private final EmailService emailService;

@Autowired
    public EmailContoller(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendMail")
    public String sendMail(@RequestBody EmailDetails details){
    return emailService.sendSimpleMail(details);
}
}
