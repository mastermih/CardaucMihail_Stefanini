package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.NotificationDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserNotificationDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.entity.UserNotification;
import com.ImperioElevator.ordermanagement.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationDaoImpl notificationDao;
    private final UserNotificationDaoImpl userNotificationDao;

    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    public NotificationServiceImpl(NotificationDaoImpl notificationDao, UserNotificationDaoImpl userNotificationDao) {
        this.notificationDao = notificationDao;
        this.userNotificationDao = userNotificationDao;
    }

    @Override
    public Long insert(Notification entity) throws SQLException {
        return notificationDao.insert(entity);
    }

    @Override
    public List<Notification> getNotifications(Long userId) throws SQLException {
        return notificationDao.getNotifications(userId);
    }

    @Override
    public Long insertUserNotification(UserNotification userNotification) throws SQLException {
        return userNotificationDao.insertUserNotification(userNotification);
    }

    @Override
    public Long notificationIsRead(Long userId) throws SQLException {
        return userNotificationDao.notificationIsRead(userId);
    }
}
