package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;

public class GmailEmailService extends EmailServiceImpl{
    public GmailEmailService(OrderDaoImpl orderDao, UserDaoImpl userDao) {
        super(orderDao, userDao);
    }
}
