package com.ImperioElevator.ordermanagement.entity;



import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.valueobjects.CreateDateTime;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.UpdateDateTime;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private Id orderId;
    private User userId;
    private Status orderStatus;
    private CreateDateTime createdDate;
    private UpdateDateTime updatedDate;
    private List<OrderProduct> orderProducts;

    @JsonCreator
    public Order(@JsonProperty("id") Long id){
        this.orderId = new Id(id);
    }

    public Order(Id orderid, User userId, Status orderStatus, CreateDateTime createdDate, UpdateDateTime updatedDate) {
        this.orderId = orderid;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.orderProducts = new ArrayList<>();
    }
    public Order() {
    }

    public Id getOrderId() {
        return orderId;
    }

    public void setOrderId(Id orderId) {
        this.orderId = orderId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }

    public CreateDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(CreateDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public UpdateDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(UpdateDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", orderStatus=" + orderStatus +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", orderProducts=" + orderProducts +
                '}';
    }
}
