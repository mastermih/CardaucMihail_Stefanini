package com.ImperioElevator.ordermanagement.factory.factoryimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.NotificationDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserNotificationDaoImpl;
import com.ImperioElevator.ordermanagement.factory.ServiceFactory;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.NotificationService;
import com.ImperioElevator.ordermanagement.service.serviceimpl.EmailServiceImpl;
import com.ImperioElevator.ordermanagement.service.serviceimpl.NotificationServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class ServiceFactoryImpl implements ServiceFactory {

    private final NotificationDaoImpl notificationDao;
    private final UserNotificationDaoImpl userNotificationDao;


    public ServiceFactoryImpl(NotificationDaoImpl notificationDao, UserNotificationDaoImpl userNotificationDao, OrderDaoImpl orderDao, UserDaoImpl userDao){
        this.notificationDao = notificationDao;
        this.userNotificationDao = userNotificationDao;

    }
    @Override
    public NotificationService notificationService() {
        return new NotificationServiceImpl(notificationDao, userNotificationDao);
    }

}
