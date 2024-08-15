package com.ImperioElevator.ordermanagement.valueobjects;


import com.ImperioElevator.ordermanagement.entity.Category;
import com.ImperioElevator.ordermanagement.enumobects.CategoryType;

public class FilterComponents {
    private Double price;
    private CategoryType categoryType;
    private ProductBrand productBrand;
    private ProductName productName;
    private Double electricityConsumption;


    public FilterComponents(Double price, CategoryType categoryType, ProductBrand productBrand, ProductName productName, Double electricityConsumption) {

        this.price = price;
        this.categoryType = categoryType;
        this.productBrand = productBrand;
        this.productName = productName;
        this.electricityConsumption = electricityConsumption;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
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

    public Double getElectricityConsumption() {
        return electricityConsumption;
    }

    public void setElectricityConsumption(Double electricityConsumption) {
        this.electricityConsumption = electricityConsumption;
    }
}
