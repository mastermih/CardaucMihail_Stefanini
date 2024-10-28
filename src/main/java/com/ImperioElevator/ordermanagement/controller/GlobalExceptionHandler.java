package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.exception.*;
import com.ImperioElevator.ordermanagement.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserExceptions(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorResponse);
    }

    @ExceptionHandler(DoublePasswordVerificationException.class)
    public ResponseEntity<ErrorResponse> handleDoublePasswordVerificationException(DoublePasswordVerificationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorResponse> AccountLockedException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.LOCKED.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.LOCKED).body(errorResponse);
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException (MaxUploadSizeExceededException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "File size exceeds the maximum limit!"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(UserRegistrationInvalidCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleUserRegistrationInvalidCredentialsException (RuntimeException ex){
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.FORBIDDEN.value(),
                    ex.getMessage()
            );
                    return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(errorResponse);
        }

        @ExceptionHandler(ThisUserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleThisUserAlreadyExistException (RuntimeException ex){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(errorResponse);
        }

    //  404 errors (resource not found)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFoundError(NoHandlerFoundException ex, WebRequest request) {
        return new ResponseEntity<>("Resource Not Found", HttpStatus.NOT_FOUND);
    }

    // all other internal errors (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleInternalErrors(Exception ex, WebRequest request) {
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }