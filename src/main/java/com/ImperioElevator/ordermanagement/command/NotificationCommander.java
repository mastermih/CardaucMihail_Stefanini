package com.ImperioElevator.ordermanagement.command;

import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.entity.UserNotification;
import com.ImperioElevator.ordermanagement.factory.factoryimpl.NotificationFactoryImpl;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.NotificationService;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
@Component
public class NotificationCommander {

    private final NotificationService notificationService;
    private final EmailService emailService;



    public NotificationCommander(NotificationService notificationService, EmailService emailService) {
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    public Long executeInAppNotification(Notification notification) throws SQLException {
        return notificationService.insert(notification);
    }

    public void executeEmailNotification(EmailDetails emailDetails, String userId) throws SQLException {
        emailService.sendConfirmationMail(emailDetails, Long.valueOf(userId));
    }

    //I do not think I will use that but it seamed to be a nice approach
    public void executeFullNotification(Notification notification, EmailDetails emailDetails, String userId) throws SQLException {
        executeInAppNotification(notification);
        if (emailDetails != null) {
            executeEmailNotification(emailDetails, userId);
        }
    }

}
