package dto;

import entity.OrderProduct;
import entity.User;
import valueobjects.CreateDateTime;
import valueobjects.UpdateDateTime;

import java.util.List;

public class UserOrderDTO {
    private User userId;
    private CreateDateTime createdDate;
    private UpdateDateTime updatedDate;
    private List<OrderProductDTO> orderProducts;

    public UserOrderDTO(User userId, CreateDateTime createdDate, UpdateDateTime updatedDate, List<OrderProductDTO> orderProducts) {
        this.userId = userId;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.orderProducts = orderProducts;
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

    public List<OrderProductDTO> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProductDTO> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
