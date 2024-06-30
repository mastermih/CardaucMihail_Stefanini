package Entity;

import java.util.List;
import java.util.Map;

public class Order
{
    //Map ? Adaugam Product product
    private int id;
    private int orderId;
    private int userId;
    private Product product;
    private int price;

    public Order(int id, int orderId, int userId, Product product, int price) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.product = product;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", userId=" + userId +
                ", product=" + product +
                ", price=" + price +
                '}';
    }
}
