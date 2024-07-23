package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.valueobjects.Number;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {

    List<Product> fiendProductForMainPage(Long limit, Long categoryId) throws SQLException;
    Product findProductById(Long id) throws SQLException;
}
