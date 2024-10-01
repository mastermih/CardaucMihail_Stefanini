package com.ImperioElevator.ordermanagement.entity;


import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Price;
import com.ImperioElevator.ordermanagement.valueobjects.Quantity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderProduct(
        @JsonProperty("orderId") Id orderId,
        @JsonProperty("order") Order order,
        @JsonProperty("quantity") Quantity quantity,
        @JsonProperty("priceOrder") Price priceOrder,
        @JsonProperty("parent_product_id") Id parentProductId,
        @JsonProperty("product") Product product
        ) {
    @JsonCreator
    public OrderProduct {
        // constructor logic if needed
    }
}