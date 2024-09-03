package com.ImperioElevator.ordermanagement.dao;

import com.ImperioElevator.ordermanagement.entity.User;

import java.sql.SQLException;

public interface UserDao extends Dao<User> {
    Long confirmUserByEmailConfirmationLocked(Long id) throws SQLException;

}
