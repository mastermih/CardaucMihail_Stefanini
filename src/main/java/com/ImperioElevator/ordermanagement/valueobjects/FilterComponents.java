package com.ImperioElevator.ordermanagement.valueobjects;


import com.ImperioElevator.ordermanagement.entity.Category;

public class FilterComponents {
    private Price price;
    private Category category;
    private ProductBrand productBrand;
    private ProductName productName;
    private ElectricityConsumption electricityConsumption;


    public FilterComponents(Price price, Category category, ProductBrand productBrand, ProductName productName, ElectricityConsumption electricityConsumption) {

        this.price = price;
        this.category = category;
        this.productBrand = productBrand;
        this.productName = productName;
        this.electricityConsumption = electricityConsumption;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
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
}
