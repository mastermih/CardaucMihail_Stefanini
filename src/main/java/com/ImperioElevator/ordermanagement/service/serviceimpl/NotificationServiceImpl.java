package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl {
    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    public NotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    public void sendNotification(Long userId, Notification notification){
        logger.debug("Sending WS notifications to user {} with payload {}",  userId, notification);
        //ToDO add the user role of who  have to get this notification
        messagingTemplate.convertAndSendToUser(String.valueOf(userId),
                "/notification",
                notification);
    }
}
