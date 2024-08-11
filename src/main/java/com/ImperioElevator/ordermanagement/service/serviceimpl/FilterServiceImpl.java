package com.ImperioElevator.ordermanagement.service.serviceimpl;



import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.valueobjects.FilterComponents;
import com.ImperioElevator.ordermanagement.service.FilterService;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterServiceImpl implements FilterService {

    @Override
    public List<Product> filter(List<Product> products, FilterComponents filterComponents) {
         Predicate<Product> priceProduct = (product) -> filterComponents.getPrice() == null || product.price().equals(filterComponents.getPrice());
         Predicate<Product> categoryProduct = (product) -> filterComponents.getCategory() == null || product.category().equals(filterComponents.getCategory());
         Predicate<Product> productBrand = (product) -> filterComponents.getProductBrand() == null || product.productBrand().equals(filterComponents.getProductBrand());
         Predicate<Product> electriciyProduct = (prodct) -> filterComponents.getElectricityConsumption() == null || prodct.electricityConsumption().equals(filterComponents.getElectricityConsumption());
         return products.stream()
                 .filter(priceProduct)
                 .filter(categoryProduct)
                 .filter(productBrand)
                 .filter(electriciyProduct)
                 .collect(Collectors.toList());
    }
}
