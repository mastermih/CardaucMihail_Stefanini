package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Role;

import java.sql.SQLException;

public interface UserSevice {
    Long addNewUser (User user) throws SQLException;
    Long confirmUserByEmail (Long id) throws SQLException;
}
