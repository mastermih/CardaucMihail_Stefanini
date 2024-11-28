package com.ImperioElevator.ordermanagement.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PriceWithVAT(@JsonProperty("price_with_VAT") BigDecimal priceWithVAT) {
    @JsonCreator
    public PriceWithVAT {
    }
}
