package com.ImperioElevator.ordermanagement.entity;

import com.ImperioElevator.ordermanagement.enumobects.Role;
import com.ImperioElevator.ordermanagement.valueobjects.Email;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Name;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
//ToDo fix the usr validation for name and Email
public record User(
        Id userId,
        @NotNull
                //Size is not working user creation fail
       // @Size(min = 2, max = 20, message = "Not less then 2 and not more then 20")
        Name name,
        //@jakarta.validation.constraints.Email(message = "Email should be valid")
        Email email,
       //@JsonIgnore // because of this user can not be registered
     //   @Size(min = 5, max = 30, message = " The password number of characters is invalid")
        String password,
    //    @Size(min = 5, max = 15, message = "The phone, number of characters is invalid")
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
