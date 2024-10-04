package com.ImperioElevator.ordermanagement.exception;

public class AccountLockedException extends RuntimeException{
    public AccountLockedException (Long id){
        super(String.valueOf(id));
    }
}
