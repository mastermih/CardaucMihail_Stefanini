package com.ImperioElevator.ordermanagement.entity;

import com.ImperioElevator.ordermanagement.enumobects.Role;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.valueobjects.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
public record Order(
        @JsonProperty("orderId") Id orderId,
        @JsonProperty("userId") User userId,
        @JsonProperty("orderStatus") Status orderStatus,
        @JsonProperty("createdDate") CreateDateTime createdDate,
        @JsonProperty("updatedDate") UpdateDateTime updatedDate,
        @JsonProperty("orderInvoice")OrderInvoice orderInvoice,
        @JsonProperty("orderProducts") List<OrderProduct> orderProducts
) {
    @JsonCreator
    public Order {
        if (orderProducts == null) {
            orderProducts = List.of();
        }

    }
}

