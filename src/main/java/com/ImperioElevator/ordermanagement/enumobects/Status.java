package com.ImperioElevator.ordermanagement.enumobects;

public enum Status {
    OPEN,
    NEW, // This state comes after the Intialisation state / After user made the order  -2
    CLOSED,
    INITIALISED, // First state of the order / After it was added to cart by user   -1
    CANCELED, // This state comes after the New state / After user removed the item from the cart   - ?
    IN_PROGRESS,
    CONFIRMED, // This state comes after the New state / After user confirmed the order throw Email  -3
    DECLINED_BY_CLIENT,
    EXPIRED
}
