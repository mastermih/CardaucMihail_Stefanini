package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.command.NotificationCommanderInterface;
import com.ImperioElevator.ordermanagement.command.commandImpl.NotificationCommander;
import com.ImperioElevator.ordermanagement.dao.daoimpl.NotificationDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserNotificationDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.UserNotification;
import com.ImperioElevator.ordermanagement.factory.factoryimpl.NotificationFactoryImpl;
import com.ImperioElevator.ordermanagement.service.NotificationService;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationDaoImpl notificationDao;
    private final UserNotificationDaoImpl userNotificationDao;
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    public NotificationServiceImpl(NotificationDaoImpl notificationDao, UserNotificationDaoImpl userNotificationDao) {
        this.notificationDao = notificationDao;
        this.userNotificationDao = userNotificationDao;


    }

    @Override
    public Long insert(Notification entity) throws SQLException {
            return notificationDao.insert(entity);
    }

//    @Override
//    public Long insertNotificationWithInvoice(Order order, ByteArrayResource attachment) throws SQLException {
//        String message = "Invoice has been created for the order with ID " + order.orderId();
//        Notification notification = notificationFactoryImpl.createInvoiceNotification(message, attachment);
//        Long notificationId = notificationCommander.executeInAppNotification(notification);

        //List<String> operators = ordersService.getOperatorAssignedToOrder(order.orderId().id());
//        operators.forEach(operatorName -> {
//            try {
//                Long operatorId = userService.userDao.findUserIdByName(operatorName);
//                if (operatorId != null) {
//                    UserNotification userNotification = notificationFactoryImpl.createUserNotificationAssigneeOperatorToOrder(notificationId, operatorId);
//                    insertUserNotification(userNotification);
//                } else {
//                    logger.error("No user ID found for operator: " + operatorName);
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return order.orderId().id();
//    }

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
          return userNotificationDao.insertUserNotification(userNotification);
      }

    @Override
    public Long notificationIsRead(Long userId) throws SQLException {
            return userNotificationDao.notificationIsRead(userId);
    }

    @Override
    public Long notificationIsDisabled(Long notificationId, Long userId) throws SQLException {
            return userNotificationDao.notificationIsDisabled(notificationId, userId);

        }


}
