package com.ImperioElevator.ordermanagement.factory;

import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.NotificationService;


public interface ServiceFactory {
    NotificationService notificationService();
}
