package com.ImperioElevator.ordermanagement.command.commandImpl;

import com.ImperioElevator.ordermanagement.command.NotificationCommanderInterface;
import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.NotificationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class NotificationCommander implements NotificationCommanderInterface {

    private final NotificationService notificationService;
    private final EmailService emailService;

    public NotificationCommander( NotificationService notificationService, EmailService emailService) {
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
