package com.ImperioElevator.ordermanagement.entity;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

@Service
public class OrderCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(OrderCleanupService.class);

    @Autowired
    private OrderDaoImpl orderDao;

    @Scheduled(cron = "0 0 * * * ?") // odata in ora
    public void cancelOrder() throws SQLException {

        int expiredOrders = Math.toIntExact(orderDao.deleteUnconfirmedOrderByEmail());

        logger.info("Expired Orders: " + expiredOrders);
    }
}
