package entity;

import valueobjects.Email;
import valueobjects.Id;
import valueobjects.Name;

public class User
{
    private Id userId;
    private Name name;
    private Email email;

    public User(Id userId, Name name, Email email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public Id getUserId() {
        return userId;
    }

    public void setUserId(Id userId) {
        this.userId = userId;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name=" + name +
                ", email=" + email +
                '}';
    }
}
