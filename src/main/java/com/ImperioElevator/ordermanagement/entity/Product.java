package com.ImperioElevator.ordermanagement.entity;


import com.ImperioElevator.ordermanagement.enumobects.CategoryType;
import com.ImperioElevator.ordermanagement.valueobjects.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

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
        Image path,
        CategoryType categoryType) {

}