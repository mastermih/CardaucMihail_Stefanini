package com.ImperioElevator.ordermanagement.valueobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public record Name(
        @NotNull
        @Size(min = 2, max = 20, message = "Not less then 2 and not more then 20")
        @JsonProperty("name") String name
)
{
}
