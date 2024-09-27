package com.ImperioElevator.ordermanagement.dto;

import com.ImperioElevator.ordermanagement.entity.Order;

public class OrdersFoundLastCreatedDTO {
    private Order order;
    private String userName;
    private String operatorUserIds;
    private String creatorUsername;

    public OrdersFoundLastCreatedDTO(Order order, String userName, String operatorUserIds, String creatorUsername) {
        this.order = order;
        this.userName = userName;
        this.operatorUserIds = operatorUserIds;
        this.creatorUsername = creatorUsername;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOperatorUserIds() {
        return operatorUserIds;
    }

    public void setOperatorUserIds(String operatorUserIds) {
        this.operatorUserIds = operatorUserIds;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }
}
