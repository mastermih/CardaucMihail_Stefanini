package com.ImperioElevator.ordermanagement.entity;


import com.ImperioElevator.ordermanagement.valueobjects.*;

public class Product {
    private Id productId;
    private Price price;
    private Width width;
    private Height height;
    private Depth depth;
    private Category category;
    private ProductBrand productBrand;
    private ProductName productName;
    private ElectricityConsumption electricityConsumption;
    private Description description;

    public Product(Id productId, Price price, Width width, Height height, Depth depth, Category category,
                   ProductBrand productBrand, ProductName productName, ElectricityConsumption electricityConsumption,
                   Description description) {
        this.productId = productId;
        this.price = price;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.category = category;
        this.productBrand = productBrand;
        this.productName = productName;
        this.electricityConsumption = electricityConsumption;
        this.description = description;
    }

    public Id getProductId() {
        return productId;
    }

    public void setProductId(Id productId) {
        this.productId = productId;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Width getWidth() {
        return width;
    }

    public void setWidth(Width width) {
        this.width = width;
    }

    public Height getHeight() {
        return height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public Depth getDepth() {
        return depth;
    }

    public void setDepth(Depth depth) {
        this.depth = depth;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ProductBrand getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(ProductBrand productBrand) {
        this.productBrand = productBrand;
    }

    public ProductName getProductName() {
        return productName;
    }

    public void setProductName(ProductName productName) {
        this.productName = productName;
    }

    public ElectricityConsumption getElectricityConsumption() {
        return electricityConsumption;
    }

    public void setElectricityConsumption(ElectricityConsumption electricityConsumption) {
        this.electricityConsumption = electricityConsumption;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", price=" + price +
                ", width=" + width +
                ", height=" + height +
                ", depth=" + depth +
                ", category=" + category +
                ", productBrand=" + productBrand +
                ", productName=" + productName +
                ", electricityConsumption=" + electricityConsumption +
                ", description=" + description +
                '}';
    }
}