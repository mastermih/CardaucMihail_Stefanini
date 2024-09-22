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
      // @JsonIgnore // because of this user can not be registrated
        String password,//ToDo check the password on Ui and Back add the validation and doucble password insertions //in service layers in con controler add the validation//
        String phoneNumber,
        String image,
        List<Role> roles,
        boolean accountNonLocked
) {
    public User(Id id1, Long id, Object o, Object object, Object o1, Object object1, Object o2, Object object2, boolean b) {
        this(new Id(id), new Name(""), new Email(""), "","", "",List.of(Role.USER), true);
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
