package com.ImperioElevator.ordermanagement.entity;

import com.ImperioElevator.ordermanagement.enumobects.Role;
import com.ImperioElevator.ordermanagement.valueobjects.Email;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Image;
import com.ImperioElevator.ordermanagement.valueobjects.Name;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record User(
        Id userId,
        Name name, // This should be managed if it can be null
        Email email,
        String password,
        Role role,
        boolean accountNonLocked
) {
    @JsonCreator
    public User(@JsonProperty("id") Long id) {
        this(new Id(id), new Name(""), new Email(""), "", Role.USER, true);
    }
}

