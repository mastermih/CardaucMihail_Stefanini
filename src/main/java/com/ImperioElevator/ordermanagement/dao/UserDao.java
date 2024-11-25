package com.ImperioElevator.ordermanagement.dao;

import com.ImperioElevator.ordermanagement.entity.User;
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
    Long findUserIdByName(String name) throws SQLException;//For notification of the user when manager assign an order to sale_manager this manager should be notified

    User fiendUserByToken(String token) throws SQLException; //For accessing the Profile with out seeing the id
    Long createUserUnauthorized(User user);
    Boolean registrationThatUserCredentialsAlreadyExists(String name, String email);
    List<User> getManagementUsers() throws SQLException;
    List<User> findAllUsers();
    //ToDo add the rest of the requests
    //Paginable<User> fiendAllPaginableUsersManagemnt();
}
