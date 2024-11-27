package com.ImperioElevator.ordermanagement.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DiscountPercentages(@JsonProperty("discount_percentages") Long discount_percentages) {
    @JsonCreator
    public  DiscountPercentages{

    }
}
