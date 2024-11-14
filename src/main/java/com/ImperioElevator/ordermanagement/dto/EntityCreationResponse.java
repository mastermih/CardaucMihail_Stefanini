package com.ImperioElevator.ordermanagement.dto;

//From entity is now in the dto package
public class EntityCreationResponse {
    private Long orderId;
    private String message;

    public EntityCreationResponse(Long orderId, String message) {
        this.orderId = orderId;
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
