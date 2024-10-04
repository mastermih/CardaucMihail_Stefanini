package com.ImperioElevator.ordermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Here I handle the Login error email not found and (User Creation) verify double password
    @ExceptionHandler({LoginUserNotFoundException.class, DoublePasswordVerificationException.class})
    public ResponseEntity<ErrorResponse> handleUserExceptions(RuntimeException ex) {
        HttpStatus status = (ex instanceof LoginUserNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }

//    // Handle DoublePasswordVerificationException
//    @ExceptionHandler(DoublePasswordVerificationException.class)
//    public ResponseEntity<ErrorResponse> handleDoublePasswordVerificationException(DoublePasswordVerificationException ex) {
//        ErrorResponse errorResponse = new ErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                ex.getMessage() != null ? ex.getMessage() : "Password verification failed"
//        );
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorResponse> AccountLockedException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.LOCKED.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.LOCKED).body(errorResponse);
    }
}