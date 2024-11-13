package com.ImperioElevator.ordermanagement.factory;

import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.service.EmailService;

public interface EmailServiceFactory {
    //User confirm the order, so this update the order status to INITIALISED
    EmailDetails createEmailServiceUpdateOrderStatus(String message);

    EmailDetails createEmailServiceUserCreation(String message);
}
