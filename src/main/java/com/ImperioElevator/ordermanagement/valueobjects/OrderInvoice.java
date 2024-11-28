package com.ImperioElevator.ordermanagement.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

    public record OrderInvoice (@JsonProperty("order_invoice") String orderInvoice) {
        @JsonCreator
        public OrderInvoice{

        }
    }

