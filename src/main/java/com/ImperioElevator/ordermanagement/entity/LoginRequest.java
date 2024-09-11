package com.ImperioElevator.ordermanagement.entity;

import com.ImperioElevator.ordermanagement.valueobjects.Email;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Name;

public record LoginRequest (
        String email,
        String password
){

}
