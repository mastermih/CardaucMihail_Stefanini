package com.ImperioElevator.ordermanagement.valueobjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductName (@JsonProperty("product_name") String productName)
{

}
