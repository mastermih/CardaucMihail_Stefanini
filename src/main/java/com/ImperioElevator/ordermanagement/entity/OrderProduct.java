package com.ImperioElevator.ordermanagement.entity;


import com.ImperioElevator.ordermanagement.valueobjects.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderProduct(
        @JsonProperty("orderId") Id orderId,
        @JsonProperty("order") Order order,
        @JsonProperty("quantity") Quantity quantity,
        @JsonProperty("priceOrder") Price priceOrder,
        @JsonProperty("discount_percentages") DiscountPercentages discount_percentages,
        @JsonProperty("price_discount") Price price_discount,
        @JsonProperty("VAT") Vat VAT,
        @JsonProperty("price_with_VAT") Price price_with_VAT,
        @JsonProperty("parent_product_id") Id parentProductId,
        @JsonProperty("product") Product product
        ) {
    @JsonCreator
    public OrderProduct {
        // constructor logic if needed
    }
}