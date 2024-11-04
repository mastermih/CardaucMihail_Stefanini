package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.dto.UserRegistrationDTO;
import com.ImperioElevator.ordermanagement.entity.EntityCreationResponse;
import com.ImperioElevator.ordermanagement.entity.LoginRequest;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.entity.UserCreationResponse;
import com.ImperioElevator.ordermanagement.enumobects.Role;
import com.ImperioElevator.ordermanagement.exception.DoublePasswordVerificationException;
import com.ImperioElevator.ordermanagement.exception.LoginUserNotFoundException;
import com.ImperioElevator.ordermanagement.exception.UserRegistrationInvalidCredentialsException;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.UserSevice;
import com.ImperioElevator.ordermanagement.service.serviceimpl.EmailServiceImpl;
import com.ImperioElevator.ordermanagement.service.serviceimpl.UserServiceImpl;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import io.swagger.v3.oas.annotations.Operation;
//import liquibase.sql.Sql;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.sql.SQLException;
import java.util.List;

//ToDo add comments for all methods in the controller Swagger form
@RestController
public class UserController {
    private final UserSevice userSevice;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);
    public UserController(UserSevice userSevice, EmailServiceImpl emailService) {
        this.userSevice = userSevice;
        this.emailService = emailService;
    }
    @PostMapping("createUser/Superior")
    public ResponseEntity<UserCreationResponse> addNewUserSuperior(@RequestBody User user)throws SQLException{
          Long userId = userSevice.addNewUser(user);
        UserCreationResponse response = new UserCreationResponse(
                  userId,
                  "User " + userId + " was added successfully"
          );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/sendMail/confirm/user/{token}")
    public String sendConfirmationUserEmailStatus(@PathVariable String token) throws SQLException {
        return emailService.updateUserEmailConfirmStatus(token);
    }


    @PostMapping("createUser")
    public ResponseEntity<UserCreationResponse> addNewUser(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO, BindingResult result)throws SQLException{
        User user = userRegistrationDTO.getUser();
        String verifyPassword = userRegistrationDTO.getVerifyPassword();

        if(!user.password().equals(verifyPassword)){
            throw new DoublePasswordVerificationException("Passwords do not match");
        }

        if (result.hasErrors()) {
         throw new UserRegistrationInvalidCredentialsException("Registration Contains Invalid Credentials ");
        }
        Long userId = userSevice.addNewUser(userRegistrationDTO.getUser());

        UserCreationResponse response = new UserCreationResponse(
                userId,
                "User " + userId + " was added success"
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/uploadImage")
    public String getUserImage(@RequestParam Long userId) throws SQLException{
        return userSevice.getUserImage(userId);
    }

    // THe original one for getting the Profile of user by id  // Maybe I can use this one but with no id as path variable
    @GetMapping("/UserProfile")
    public User getUserProfile(@RequestParam Long userId) throws SQLException{
        return userSevice.getUserProfile(userId);
    }


    @PutMapping("/UserProfile")
    public Long updateUser(@RequestBody User user) throws  SQLException{
        return userSevice.updateUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest user, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "Error Login, invalid data";
        }
        return userSevice.verifyUser(user);
    }
    @GetMapping("/ViewUsers/allUsers")
    public List<User> ViewUsers()throws SQLException{
        return userSevice.findAllUsers();
    }
//    @GetMapping("/GetManagementUsers")
//    public List<User>
}
