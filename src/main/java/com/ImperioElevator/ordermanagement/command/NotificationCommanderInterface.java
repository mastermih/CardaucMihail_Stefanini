package com.ImperioElevator.ordermanagement.command;

import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.entity.Notification;

import java.sql.SQLException;

public interface NotificationCommanderInterface {
    Long executeInAppNotification(Notification notification) throws SQLException;
    void executeEmailNotification(EmailDetails emailDetails, String userId) throws SQLException;
    void executeFullNotification(Notification notification, EmailDetails emailDetails, String userId) throws SQLException;
}
