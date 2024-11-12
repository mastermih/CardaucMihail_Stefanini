package com.ImperioElevator.ordermanagement.factory.factoryimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;
import com.ImperioElevator.ordermanagement.factory.EmailServiceFactory;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.serviceimpl.EmailServiceImpl;
import com.ImperioElevator.ordermanagement.service.serviceimpl.GmailEmailService;
import com.ImperioElevator.ordermanagement.service.serviceimpl.YandexEmailService;

import java.util.Locale;

public class EmailFactoryImpl implements EmailServiceFactory {


    private final OrderDaoImpl orderDao;
    private final UserDaoImpl userDao;

    public EmailFactoryImpl(OrderDaoImpl orderDao, UserDaoImpl userDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
    }
    //ToDO Service ce trimite email to ce primieste  mesage in dependenta the  parametrii//
    //Factory la notification object diferite filduri //la creiarea notificarilor  unde trimit parametrii si creiaza  obiectul de notificare

    @Override
    public EmailService createEmailService(String provider) {
        switch (provider.toLowerCase()){
            case "gmail":
                return new GmailEmailService(orderDao, userDao);
            case "yandex":
                return new YandexEmailService(orderDao,userDao);
            default:
                throw new IllegalArgumentException("Unsuported email provider " + provider);
        }
    }
}
