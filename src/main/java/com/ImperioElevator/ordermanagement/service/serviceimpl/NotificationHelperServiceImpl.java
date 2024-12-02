package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.command.NotificationCommanderInterface;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserNotificationDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.UserNotification;
import com.ImperioElevator.ordermanagement.factory.factoryimpl.NotificationFactoryImpl;
import com.ImperioElevator.ordermanagement.service.NotificationHelperService;
import com.ImperioElevator.ordermanagement.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class NotificationHelperServiceImpl implements NotificationHelperService {

    private final NotificationFactoryImpl notificationFactoryImpl;
    //private final NotificationCommanderInterface notificationCommander;
    private final UserServiceImpl userService;
    private final UserNotificationDaoImpl userNotificationDao;
    private final NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    public NotificationHelperServiceImpl(NotificationService notificationService,UserNotificationDaoImpl userNotificationDao,NotificationFactoryImpl notificationFactoryImpl, NotificationCommanderInterface notificationCommander,UserServiceImpl userService){
   this.notificationFactoryImpl = notificationFactoryImpl;
//   this.notificationCommander = notificationCommander;
   this.userService = userService;
   this.userNotificationDao = userNotificationDao;
   this.notificationService = notificationService;
    }

        @Override
    public Long insertNotificationWithInvoice(Order order, ByteArrayResource attachment, List<String>operators) throws SQLException {
        String message = "Invoice has been created for the order with ID " + order.orderId();
        Notification notification = notificationFactoryImpl.createInvoiceNotification(message, attachment);
        Long notificationId = notificationService.insert(notification);
        operators.forEach(operatorName -> {
            try {
                Long operatorId = userService.userDao.findUserIdByName(operatorName);
                if (operatorId != null) {
                    UserNotification userNotification = notificationFactoryImpl.createUserNotificationAssigneeOperatorToOrder(notificationId, operatorId);
                    userNotificationDao.insertUserNotification(userNotification);
                } else {
                    logger.error("No user ID found for operator: " + operatorName);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return order.orderId().id();
    }
}
