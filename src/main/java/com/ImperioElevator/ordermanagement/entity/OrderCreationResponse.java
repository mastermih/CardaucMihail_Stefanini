package com.ImperioElevator.ordermanagement.entity;

public class OrderCreationResponse {
    private Long orderId;
    private String message;
    private int numberOfProducts;

    public OrderCreationResponse(Long orderId, String message, int numberOfProducts) {
        this.orderId = orderId;
        this.message = message;
        this.numberOfProducts = numberOfProducts;
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

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }
}
