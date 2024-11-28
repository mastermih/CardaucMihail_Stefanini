package com.ImperioElevator.ordermanagement.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record Price(@JsonProperty("price") BigDecimal price) {
    @JsonCreator
    public Price {
        // This compact constructor ensures the class invariants
    }
}
