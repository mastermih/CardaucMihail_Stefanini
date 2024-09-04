package com.ImperioElevator.ordermanagement.entity;

import com.ImperioElevator.ordermanagement.enumobects.Role;
import com.ImperioElevator.ordermanagement.valueobjects.Email;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Name;

public record User(
        Id userId,
        Name name,
        Email email,
        String password,
        Role role,
        boolean accountNonLocked // Primitive boolean instead of Boolean
) {
    // Constructor for creating User with only ID
    public User(Long id) {
        this(new Id(id), new Name(""), new Email(""), "", Role.USER, true);
    }

    // Full constructor for User
    public User(Id userId, Name name, Email email, String password, Role role, boolean accountNonLocked) {
        this.userId = userId != null ? userId : new Id(0L);
        this.name = name != null ? name : new Name("");
        this.email = email != null ? email : new Email("");
        this.password = password != null ? password : "";
        this.role = role != null ? role : Role.USER;
        this.accountNonLocked = accountNonLocked; // Correctly mapped primitive boolean
    }
}
