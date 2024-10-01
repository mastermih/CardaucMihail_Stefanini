package com.ImperioElevator.ordermanagement.valueobjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Name(
        @JsonProperty("name") String name
)
{
}
