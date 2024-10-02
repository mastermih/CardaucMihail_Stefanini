package com.ImperioElevator.ordermanagement.entity;

import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Name;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record LoginRequest (
        @Email
        String email,
        @Size(min = 3, max = 15, message = "The phone, number of characters is invalid")
        String password
){

}