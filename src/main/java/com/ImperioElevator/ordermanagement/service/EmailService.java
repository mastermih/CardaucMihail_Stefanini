package com.ImperioElevator.ordermanagement.service;


import com.ImperioElevator.ordermanagement.entity.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);

}
