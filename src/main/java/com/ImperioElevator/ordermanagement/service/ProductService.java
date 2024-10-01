package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.valueobjects.FilterComponents;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {

    List<Product> fiendProductForMainPage(Long limit, String categoryType) throws SQLException;
    Product findProductById(Long id) throws SQLException;
    List<Product> findProductByName(String name) throws SQLException;
    Paginable<Product> filterProducts(FilterComponents filterComponents, Long page, Long pageSize) throws SQLException;

}
