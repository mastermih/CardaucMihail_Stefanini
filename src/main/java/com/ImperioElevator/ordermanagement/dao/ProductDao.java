package com.ImperioElevator.ordermanagement.dao;

import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.enumobects.CategoryType;
import com.ImperioElevator.ordermanagement.valueobjects.FilterComponents;
import com.ImperioElevator.ordermanagement.valueobjects.Name;
import com.ImperioElevator.ordermanagement.valueobjects.Number;

import java.util.List;

public interface ProductDao extends Dao<Product> {
   List<Product> fiendProductForMainPage(Long limit, String categoryType);
   List<Product> fiendProductByName(String name);
   List<Product> filterProducts(FilterComponents filterComponents);
}
