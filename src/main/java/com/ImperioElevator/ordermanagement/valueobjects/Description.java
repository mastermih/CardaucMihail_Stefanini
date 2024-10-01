package com.ImperioElevator.ordermanagement.valueobjects;

public class Description
{
   private String description;

    public Description(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Description{" +
                "description='" + description + '\'' +
                '}';
    }
}
