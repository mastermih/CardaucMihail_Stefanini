package com.ImperioElevator.ordermanagement.valueobjects;

public class Quantity {
    private int quantity;

    public Quantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Quantity{" +
                "quantity=" + quantity +
                '}';
    }
}
