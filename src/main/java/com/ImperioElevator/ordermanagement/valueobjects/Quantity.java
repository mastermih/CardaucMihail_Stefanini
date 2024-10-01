package com.ImperioElevator.ordermanagement.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Quantity(@JsonProperty("quantity") int quantity) {
    @JsonCreator
    public Quantity {
        // This compact constructor ensures the class invariants
    }
}
