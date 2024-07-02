package entity;

import valueobjects.CreateDateTime;
import valueobjects.Id;
import valueobjects.UpdateDateTime;

// ToDo Doua entitati la timp create si updated
public class Order
{
    private Id orderId;
    private User userId;
    //alternativa
    private CreateDateTime createdDate;
    private UpdateDateTime updatedDate;

    public Order(Id orderId, User userId, CreateDateTime createdDate, UpdateDateTime updatedDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
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

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
