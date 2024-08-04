package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {

    List<Product> fiendProductForMainPage(Long limit, String categoryType) throws SQLException;
    Product findProductById(Long id) throws SQLException;
    List<Product> findProductByName(String name) throws SQLException;
}
