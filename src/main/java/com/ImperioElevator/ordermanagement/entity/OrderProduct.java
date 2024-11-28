package com.ImperioElevator.ordermanagement.entity;


import com.ImperioElevator.ordermanagement.valueobjects.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record OrderProduct(
        @JsonProperty("orderId") Id orderId,
        @JsonProperty("order") Order order,
        @JsonProperty("quantity") Quantity quantity,
        @JsonProperty("priceOrder") Price priceOrder,
        @JsonProperty("discount_percentages") DiscountPercentages discount_percentages,
        @JsonProperty("price_discount") PriceDiscount price_discount,
        @JsonProperty("VAT") Vat VAT,
        @JsonProperty("price_with_VAT") PriceWithVAT price_with_VAT,
        @JsonProperty("parent_product_id") Id parentProductId,
        @JsonProperty("product") Product product
        ) {
    @JsonCreator
    public OrderProduct {
        discount_percentages = (discount_percentages == null) ? new DiscountPercentages(0L) : discount_percentages;
        VAT = (VAT == null) ? new Vat(20L) : VAT;
        price_discount = (price_discount == null) ? new PriceDiscount(BigDecimal.ZERO) : price_discount;
        price_with_VAT = (price_with_VAT == null) ? new PriceWithVAT(BigDecimal.ZERO) : price_with_VAT;
    }
}