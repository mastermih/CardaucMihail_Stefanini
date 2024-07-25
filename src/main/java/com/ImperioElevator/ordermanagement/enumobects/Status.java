package com.ImperioElevator.ordermanagement.enumobects;

public enum Status {
    OPEN,
    NEW,
    CLOSED,
    INITIALISED, // After it was added to cart
    CANCELED, // Befeore it is set to New
    IN_PROGRESS,
    DECLINED_BY_CLIENT
}
