package com.ImperioElevator.ordermanagement.dao;

import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Role;

import java.sql.SQLException;

public interface UserDao extends Dao<User> {
    Long confirmUserByEmailConfirmationLocked(Long id) throws SQLException;
    Long giveToUserARole(Long userId,Role role) throws SQLException;
}
