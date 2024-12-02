package com.ImperioElevator.ordermanagement.dao;

import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.UserNotification;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;

public interface NotificationDao extends Dao<Notification> {

    List<Notification> getNotifications(Long userId);
    Long insertNotificationWithAttachment(Notification entity, ByteArrayResource attachment);
}
