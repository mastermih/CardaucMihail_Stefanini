package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.NotificationDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserNotificationDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.entity.UserNotification;
import com.ImperioElevator.ordermanagement.exception.NotificationException;
import com.ImperioElevator.ordermanagement.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
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
        //try {
            return notificationDao.insert(entity);
        //}catch (Exception e){
         //   throw new NotificationException("The notification service failed :( ");
       // }
    }

    @Override
    public List<Notification> getNotifications(Long userId) {
        List<Notification> notifications = new ArrayList<>();
        try {
            notifications = notificationDao.getNotifications(userId);
        } catch (Exception e) {
            return notifications;
        }
        return notifications;
    }

    @Override
    public Long insertUserNotification(UserNotification userNotification) throws SQLException {
    //  try {
          userNotification.setDisabled(false);
          return userNotificationDao.insertUserNotification(userNotification);
    //  }catch (Exception e){
    //      throw new NotificationException("The notification service failed :( ");
   //   }
      }

    @Override
    public Long notificationIsRead(Long userId) throws SQLException {
      //  try {
            return userNotificationDao.notificationIsRead(userId);
     //   }catch (Exception e){
      //      throw new NotificationException("The notification service failed :( ");
       // }
    }

    @Override
    public Long notificationIsDisabled(Long notificationId, Long userId) throws SQLException {
    //    try {
            return userNotificationDao.notificationIsDisabled(notificationId, userId);
       // }catch (Exception e){
       //     throw new NotificationException("The notification service failed :( ");
     //   }
        }
}
