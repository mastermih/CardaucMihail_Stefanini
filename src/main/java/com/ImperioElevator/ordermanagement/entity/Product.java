package com.ImperioElevator.ordermanagement.entity;


import com.ImperioElevator.ordermanagement.valueobjects.*;

public record Product(
        Id productId,
        Price price,
        Width width,
        Height height,
        Depth depth,
        Category category,
        ProductBrand productBrand,
        ProductName productName,
        ElectricityConsumption electricityConsumption,
        Description description,
        Image path) {
}