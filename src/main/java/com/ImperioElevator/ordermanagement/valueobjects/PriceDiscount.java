package com.ImperioElevator.ordermanagement.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PriceDiscount(@JsonProperty("price_discount") BigDecimal price) {
        @JsonCreator
        public PriceDiscount {
            // This compact constructor ensures the class invariants
        }
}
