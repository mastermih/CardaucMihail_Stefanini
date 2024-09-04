package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.service.UserSevice;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
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

    public UserController(UserSevice userSevice) {
        this.userSevice = userSevice;
    }
    @PostMapping("createUser/Superior")
    public Long addNewUserSuperior(@RequestBody User user)throws SQLException{
        return userSevice.addNewUser(user);
    }

}
