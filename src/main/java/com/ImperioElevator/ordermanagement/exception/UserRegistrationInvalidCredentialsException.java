package com.ImperioElevator.ordermanagement.exception;

public class UserRegistrationInvalidCredentialsException extends RuntimeException{
    public UserRegistrationInvalidCredentialsException (String message){
        super(message);
    }
}
