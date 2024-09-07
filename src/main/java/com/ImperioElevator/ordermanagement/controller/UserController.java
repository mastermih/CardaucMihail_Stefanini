package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Role;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.UserSevice;
import com.ImperioElevator.ordermanagement.service.serviceimpl.EmailServiceImpl;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.sql.SQLException;

@RestController
public class UserController {
    private final UserSevice userSevice;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);
    public UserController(UserSevice userSevice, EmailServiceImpl emailService) {
        this.userSevice = userSevice;
        this.emailService = emailService;
    }
    //ToDo un response normal
    @PostMapping("createUser/Superior")
    public Long addNewUserSuperior(@RequestBody User user)throws SQLException{
        return userSevice.addNewUser(user);
    }

    @PostMapping("/sendMail/confirm/user/{token}")
    public String sendConfirmationUserEmailStatus(@PathVariable String token) throws SQLException {
        return emailService.updateUserEmailConfirmStatus(token);
    }

    @PostMapping("createUser")
    public Long addNewUser(@RequestBody User user)throws SQLException{
        return userSevice.addNewUser(user);
    }

    @GetMapping("/uploadImage")
    public String getUserImage(@RequestParam Long userId) throws SQLException{
        return userSevice.getUserImage(userId);
    }

}
