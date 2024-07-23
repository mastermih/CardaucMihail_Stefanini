package com.ImperioElevator.ordermanagement.dao;

import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.valueobjects.Number;

import java.util.List;

public interface ProductDao extends Dao<Product> {
   List<Product> fiendProductForMainPage(Long limit, Long categoryId);
}
