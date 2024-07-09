package com.ImperioElevator.ordermanagement.dto;


import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Price;
import com.ImperioElevator.ordermanagement.valueobjects.Quantity;

public class OrderProductDTO {
    private Id prdoductId;
    private Quantity quantity;
    private Price priceOrder;

    public OrderProductDTO(Id prdoductId, Quantity quantity, Price priceOrder) {
        this.prdoductId = prdoductId;
        this.quantity = quantity;
        this.priceOrder = priceOrder;
    }

    public Id getPrdoductId() {
        return prdoductId;
    }

    public void setPrdoductId(Id prdoductId) {
        this.prdoductId = prdoductId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public Price getPriceOrder() {
        return priceOrder;
    }

    public void setPriceOrder(Price priceOrder) {
        this.priceOrder = priceOrder;
    }
}
