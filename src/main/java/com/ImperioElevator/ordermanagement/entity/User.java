package com.ImperioElevator.ordermanagement.entity;

import com.ImperioElevator.ordermanagement.enumobects.Role;
import com.ImperioElevator.ordermanagement.valueobjects.Email;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Name;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
public record User(
        Id userId,
        @Valid
        Name name,
        @Valid
        Email email,
        //@JsonIgnore // because of this user can not be registered
       @Size(min = 5, max = 30, message = " The password number of characters is invalid")
        String password,
       @Size(min = 5, max = 15, message = "The phone, number of characters is invalid")
        String phoneNumber,
        String image,
        List<Role> roles,
        boolean accountNonLocked
) {
    public User(Id userId, Long id, Name name, String email, String password, String phoneNumber, String image, List<Role> roles, boolean accountNonLocked) {
        this(
                new Id(id),
                name,
                new Email(email),
                password,
                phoneNumber,
                image,
                roles != null ? roles : List.of(Role.USER), // Default role
                accountNonLocked
        );
    }

    public User(Id userId, Name name, Email email, String password, String phoneNumber, String image, List<Role> roles, boolean accountNonLocked) {
        this.userId = userId != null ? userId : new Id(0L);
        this.name = name != null ? name : new Name("");
        this.email = email != null ? email : new Email("");
        this.password = password != null ? password : "";
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
        this.image = image != null ? image : "";
        this.roles = roles != null ? roles : List.of(Role.USER);
        this.accountNonLocked = accountNonLocked;
    }
}
