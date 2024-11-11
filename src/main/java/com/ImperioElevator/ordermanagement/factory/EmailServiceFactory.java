package com.ImperioElevator.ordermanagement.factory;

import com.ImperioElevator.ordermanagement.service.EmailService;

public interface EmailServiceFactory {
    EmailService createEmailService(String provider);
}
