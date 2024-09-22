package com.ImperioElevator.ordermanagement.dto;

import com.ImperioElevator.ordermanagement.entity.User;

public class UserRegistrationDTO {

    private User user;
    private String verifyPassword;

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }
}
