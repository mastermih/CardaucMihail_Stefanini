package com.ImperioElevator.ordermanagement.entity;


import com.ImperioElevator.ordermanagement.valueobjects.Price;
import com.ImperioElevator.ordermanagement.valueobjects.Quantity;

public class OrderProduct {
   private Order orderId;
   private Product productId;
   private Quantity quantity;
   private Price priceOrder;

   public OrderProduct(Order orderId, Product productId, Quantity quantity, Price priceOrder) {
      this.orderId = orderId;
      this.productId = productId;
      this.quantity = quantity;
      this.priceOrder = priceOrder;
   }

   public Order getOrderId() {
      return orderId;
   }

   public void setOrderId(Order orderId) {
      this.orderId = orderId;
   }

   public Product getProductId() {
      return productId;
   }

   public void setProductId(Product productId) {
      this.productId = productId;
   }

   public Quantity getQuantity() {
      return quantity;
   }

   public void setQuantity(Quantity quantity) {
      this.quantity = quantity;
   }

   public Price getPriceOrder() {
      return priceOrder;
   }

   public void setPriceOrder(Price priceOrder) {
      this.priceOrder = priceOrder;
   }

   @Override
   public String toString() {
      return "OrderProduct{" +
              "orderId=" + orderId +
              ", productId=" + productId +
              ", quantity=" + quantity +
              ", priceOrder=" + priceOrder +
              '}';
   }
}
