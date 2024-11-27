package com.ImperioElevator.ordermanagement.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Vat(@JsonProperty("VAT")Long VAT) {
  @JsonCreator
  public Vat {

  }
}
