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


    @GetMapping("ws/notifications")
    public List<Notification> getNotifications(@RequestParam Long userId) {
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
    @PostMapping("ws/notifications/disable")
    public ResponseEntity<EntityCreationResponse> notificationIsDisabled(@RequestParam Long notificationId,
                                        @RequestParam Long userId)throws SQLException{
        notificationService.notificationIsDisabled(notificationId, userId);
        EntityCreationResponse entityCreationResponse = new EntityCreationResponse(
                userId,
                "The notification with the id " + notificationId + " was removed successfully form the notification list"
        );
        return new ResponseEntity<>(entityCreationResponse, HttpStatus.OK);
    }
}
