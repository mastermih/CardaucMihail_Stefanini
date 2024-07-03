package entity;

import enumobects.Status;
import valueobjects.CreateDateTime;
import valueobjects.Id;
import valueobjects.UpdateDateTime;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private Id orderId;
    private User userId;
    private Status orderStatus;
    private CreateDateTime createdDate;
    private UpdateDateTime updatedDate;
    private List<OrderProduct> orderProducts;

    public Order(Id orderId, User userId, Status orderStatus, CreateDateTime createdDate, UpdateDateTime updatedDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.orderProducts = new ArrayList<>();
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
