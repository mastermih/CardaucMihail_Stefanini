package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.NotificationDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationDaoImpl notificationDao;

    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    public NotificationServiceImpl(SimpMessagingTemplate messagingTemplate, NotificationDaoImpl notificationDao) {
        this.messagingTemplate = messagingTemplate;
        this.notificationDao = notificationDao;
    }
    public void sendNotification(Long userId, Notification notification){
        logger.debug("Sending WS notifications to user {} with payload {}",  userId, notification);
        messagingTemplate.convertAndSendToUser(String.valueOf(userId),
                "/notification",
                notification);
    }

    @Override
    public Long insert(Notification entity) throws SQLException {
        return notificationDao.insert(entity);
    }

    @Override
    public List<Notification> getNotificationsOfCustomerCreateOrder() throws SQLException {
        return notificationDao.getNotificationsOfCustomerCreateOrder();
    }
}
