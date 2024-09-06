package com.ImperioElevator.ordermanagement.entity;

import com.ImperioElevator.ordermanagement.enumobects.Role;
import com.ImperioElevator.ordermanagement.valueobjects.Email;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Name;

import java.util.List;

public record User(
        Id userId,
        Name name,
        Email email,
        String password,
        List<Role> roles,
        boolean accountNonLocked
) {
    public User(Long id) {
        this(new Id(id), new Name(""), new Email(""), "", List.of(Role.USER), true);
    }

    public User(Id userId, Name name, Email email, String password, List<Role> roles, boolean accountNonLocked) {
        this.userId = userId != null ? userId : new Id(0L);
        this.name = name != null ? name : new Name("");
        this.email = email != null ? email : new Email("");
        this.password = password != null ? password : "";
        this.roles = roles != null ? roles : List.of(Role.USER);
        this.accountNonLocked = accountNonLocked;
    }
}
