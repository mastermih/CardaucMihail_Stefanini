package com.ImperioElevator.ordermanagement.entity;



import com.ImperioElevator.ordermanagement.valueobjects.FilterComponents;
import com.ImperioElevator.ordermanagement.service.FilterService;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Filter implements FilterService {

    @Override
    public List<Product> filter(List<Product> products, FilterComponents filterComponents) {
        Predicate<Product> pricePredicate = p -> filterComponents.getPrice() == null || p.price().equals(filterComponents.getPrice());
        Predicate<Product> categoryPredicate = p -> filterComponents.getCategory() == null || p.category().equals(filterComponents.getCategory());
        Predicate<Product> brandPredicate = p -> filterComponents.getProductBrand() == null || p.productBrand().equals(filterComponents.getProductBrand());
        Predicate<Product> namePredicate = p -> filterComponents.getProductName() == null || p.productName().equals(filterComponents.getProductName());
        Predicate<Product> electricityPredicate = p -> filterComponents.getElectricityConsumption() == null || p.electricityConsumption().equals(filterComponents.getElectricityConsumption());
      return products.stream()
              .filter(pricePredicate)
              .filter(categoryPredicate)
              .filter(brandPredicate)
              .filter(namePredicate)
              .filter(electricityPredicate)
              .collect(Collectors.toList());
    }
}
