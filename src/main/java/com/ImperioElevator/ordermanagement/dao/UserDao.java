package com.ImperioElevator.ordermanagement.dao;

import com.ImperioElevator.ordermanagement.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
//import liquibase.sql.Sql;

import java.sql.SQLException;
import java.util.List;

public interface UserDao extends Dao<User> {
    //String confirmUserByEmailConfirmationLocked(String toke) throws SQLException;

    String confirmUserByEmailConfirmationLocked(String token) throws SQLException;

    void disableTokenAfterUserConfirmation(String token) throws SQLException;

    void giveToUserRoles(Long userId, List<Long> roleIds) throws SQLException;
    Long getRoleIdFromRoleName (String roleName) throws SQLException;
    String getTheConfirmationToken(Long id)  throws SQLException;
    Long addImageForUSer (Long userId, String  imagePath) throws SQLException;
    String getUserImage(Long userId) throws SQLException;
    User findByUserEmail (String email) throws SQLException;
    Long findUserIdByEmail(String email) throws SQLException;
    User fiendUserByToken(String token) throws SQLException; //For accessing the Profile with out seeing the id
    Long createUserUnauthorized(User user);
}
