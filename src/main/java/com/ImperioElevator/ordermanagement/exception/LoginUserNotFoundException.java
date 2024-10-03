package com.ImperioElevator.ordermanagement.exception;

public class LoginUserNotFoundException extends RuntimeException{
    public LoginUserNotFoundException(String email) {
        super("User not found with Email: " + email);
    }

}