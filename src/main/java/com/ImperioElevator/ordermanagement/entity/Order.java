package com.ImperioElevator.ordermanagement.entity;

import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.valueobjects.CreateDateTime;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.UpdateDateTime;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Order(
        @JsonProperty("orderId") Id orderId,
        @JsonProperty("userId") User userId,
        @JsonProperty("orderStatus") Status orderStatus,
        @JsonProperty("createdDate") CreateDateTime createdDate,
        @JsonProperty("updatedDate") UpdateDateTime updatedDate,
        @JsonProperty("orderProducts") List<OrderProduct> orderProducts
) {
    @JsonCreator
    public Order {
        if (orderProducts == null) {
            orderProducts = List.of();
        }
    }
}
