package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.enumobects.CategoryType;
import com.ImperioElevator.ordermanagement.valueobjects.FilterComponents;
import liquibase.sql.Sql;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {

    List<Product> fiendProductForMainPage(Long limit, String categoryType) throws SQLException;
    Product findProductById(Long id) throws SQLException;
    List<Product> findProductByName(String name) throws SQLException;
    List<Product> filterProductByCategory(CategoryType categoryType) throws SQLException;
    List<Product> filterProductByName(String name) throws SQLException;
    List<Product> filterProductByBrand(String brand) throws SQLException;
    List<Product> filterProducts(FilterComponents filterComponents) throws SQLException;

}
