package com.ImperioElevator.ordermanagement.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderCleanupService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Scheduled(cron = "0 0 * * * ?") // odata in ora
    public void canceOrder(){
        System.out.println("AZZZZAMATTTT");
        String sql = "UPDATE orders SET order_status = 'EXPIRED' " +
                "WHERE order_status = 'NEW' " +
                "AND updated_date <= NOW() - INTERVAL 1 DAY;";
       jdbcTemplate.update(sql);
        System.out.println("WAAAAAAAAAAAAAAAAAAA");
    }
}
