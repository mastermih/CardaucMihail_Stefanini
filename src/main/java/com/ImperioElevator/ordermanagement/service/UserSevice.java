package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.LoginRequest;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Role;
//import liquibase.sql.Sql;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;

public interface UserSevice {
    Long addNewUser (User user) throws SQLException;
    Long addImageForUSer (Long userId, String imagePath) throws SQLException;
    String getUserImage(Long userId)throws SQLException;
    User getUserProfile(Long userId) throws SQLException;
    Long updateUser(User user) throws SQLException;
    String verifyUser(LoginRequest user) throws SQLException;
    User fiendUserByToken(String token) throws  SQLException; // This is for userPrfole selection
    Long createUserUnauthorized(User user, String verifyPassword) throws SQLException;
}
