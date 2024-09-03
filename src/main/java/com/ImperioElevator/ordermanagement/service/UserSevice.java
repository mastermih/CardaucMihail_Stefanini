package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.User;

import java.sql.SQLException;

public interface UserSevice {
    Long addNewUser (User user) throws SQLException;
    Long confirmUserByEmail (Long id) throws SQLException;
}
