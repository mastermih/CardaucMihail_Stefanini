package com.ImperioElevator.ordermanagement.valueobjects;


import com.ImperioElevator.ordermanagement.entity.Category;
import com.ImperioElevator.ordermanagement.enumobects.CategoryType;

public class FilterComponents {
    private Double minPrice;
    private Double maxPrice;
    private String categoryType;
    private ProductBrand productBrand;
    private ProductName productName;
    private Double electricityConsumption;


    public FilterComponents(Double minPrice, Double maxPrice, String categoryType, ProductBrand productBrand, ProductName productName, Double electricityConsumption) {

        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.categoryType = categoryType;
        this.productBrand = productBrand;
        this.productName = productName;
        this.electricityConsumption = electricityConsumption;
    }


    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
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
