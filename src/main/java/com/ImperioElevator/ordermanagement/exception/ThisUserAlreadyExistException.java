package com.ImperioElevator.ordermanagement.exception;

public class ThisUserAlreadyExistException extends RuntimeException{
    public ThisUserAlreadyExistException (String message){
        super(message);
    }
}
