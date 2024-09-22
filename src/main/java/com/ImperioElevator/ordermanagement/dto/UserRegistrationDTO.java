package com.ImperioElevator.ordermanagement.dto;

import com.ImperioElevator.ordermanagement.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class UserRegistrationDTO {

    private User user;
    @NotBlank(message = "Verify password is required")
    private String verifyPassword;

    @Valid
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
