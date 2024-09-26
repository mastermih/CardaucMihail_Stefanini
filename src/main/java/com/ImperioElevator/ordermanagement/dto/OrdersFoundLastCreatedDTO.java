package com.ImperioElevator.ordermanagement.dto;

import com.ImperioElevator.ordermanagement.entity.Order;

public class OrdersFoundLastCreatedDTO {
    private Order order;
    private String userName;
  //  private Long operatorUserId;
    private String creatorUsername;

    public OrdersFoundLastCreatedDTO(Order order, String userName, String creatorUsername) {
        this.order = order;
        this.userName = userName;
       // this.operatorUserId = operatorUserId;
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

//    public Long getOperatorUserId() {
//        return operatorUserId;
//    }
//
//    public void setOperatorUserId(Long operatorUserId) {
//        this.operatorUserId = operatorUserId;
//    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }
}
