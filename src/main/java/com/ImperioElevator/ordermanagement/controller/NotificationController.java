package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.dao.daoimpl.NotificationDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
//ToDo add response Entity
    @PostMapping("ws/notifications")
    public Long insert (@RequestParam Notification entity) throws SQLException {
        return notificationService.insert(entity);
    }
    @GetMapping("ws/notifications")
    public List<Notification> getNotificationsOfCustomerCreateOrder(@RequestParam Long userId) throws SQLException {
        return notificationService.getNotificationsOfCustomerCreateOrder(userId);
    }
}
