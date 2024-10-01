package com.ImperioElevator.ordermanagement.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Price(@JsonProperty("price") double price) {
    @JsonCreator
    public Price {
        // This compact constructor ensures the class invariants
    }
}
