package com.ImperioElevator.ordermanagement.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Id(
        @JsonProperty("id") Long id
) {

}
