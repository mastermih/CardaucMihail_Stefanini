package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.dao.daoimpl.NotificationDaoImpl;
import com.ImperioElevator.ordermanagement.entity.EntityCreationResponse;
import com.ImperioElevator.ordermanagement.entity.Notification;
import com.ImperioElevator.ordermanagement.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("ws/notifications")//Maybe this is not a good approach
    public ResponseEntity<EntityCreationResponse> insert (@RequestParam Notification entity) throws SQLException {
        Long notificationId = notificationService.insert(entity);
        EntityCreationResponse entityCreationResponse = new EntityCreationResponse(
                notificationId,
                "Notification with id " + notificationId + " was created successfully"
        );
        return new ResponseEntity<>(entityCreationResponse, HttpStatus.CREATED);
    }
    @GetMapping("ws/notifications")//Combine the response list with a message
    public List<Notification> getNotifications(@RequestParam Long userId) throws SQLException {
        return notificationService.getNotifications(userId);
    }
    @PostMapping("ws/notifications/read")
    public ResponseEntity<EntityCreationResponse> notificationIsRead (@RequestParam Long userId) throws SQLException{
        notificationService.notificationIsRead(userId);
        EntityCreationResponse entityCreationResponse =  new EntityCreationResponse(
                userId,
                "The notifications of the user with id " + userId + " where read"
        );
        return new ResponseEntity<>(entityCreationResponse, HttpStatus.OK);
    }
}
