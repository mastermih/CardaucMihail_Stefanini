package com.ImperioElevator.ordermanagement.service;



import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.valueobjects.FilterComponents;

import java.util.List;

public interface FilterService{
    List<Product> filter(List<Product> products, FilterComponents filterComponents);
}
