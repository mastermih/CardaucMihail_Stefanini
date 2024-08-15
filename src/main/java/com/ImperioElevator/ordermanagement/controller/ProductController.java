package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.entity.Category;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.enumobects.CategoryType;
import com.ImperioElevator.ordermanagement.service.ProductService;
import com.ImperioElevator.ordermanagement.valueobjects.*;
import com.ImperioElevator.ordermanagement.valueobjects.Number;
import liquibase.pro.packaged.P;
import liquibase.pro.packaged.S;
import liquibase.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/catalog")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/catalog")
    public List<Product> getFiendProductForMainPage(@RequestParam("limit") Long limit,
                                                    @RequestParam("categoryType") String categoryType) throws SQLException {
        return productService.fiendProductForMainPage(limit, categoryType);
    }

    @GetMapping("/product/{id}")
    public Product getFindProductById(@PathVariable("id") Long id) throws SQLException {
        return productService.findProductById(id);
    }

    @GetMapping("MakeOrder/{id}")
    public List<Product> getFiendProductByName(@PathVariable("id") Long id, @RequestParam("product_name") String name) throws SQLException {
        return productService.findProductByName(name);
    }

    @GetMapping("catalog/categoryType")
    public List<Product> filterProductByCategory(@RequestParam("category_type") CategoryType categoryType) throws SQLException {
        return productService.filterProductByCategory(categoryType);
    }

    @GetMapping("catalog/name")
    public List<Product> filterProductByName(@RequestParam String name) throws SQLException {
        return productService.filterProductByName(name);
    }

    @GetMapping("catalog/brand")
    public List<Product> filterProductByBrand(@RequestParam String brand) throws SQLException {
        return productService.filterProductByBrand(brand);
    }

    @GetMapping("catalog/filter")
    public List<Product> filterProucts(@RequestParam(required = false) CategoryType category_type,
                                       @RequestParam(required = false) ProductName product_name,
                                       @RequestParam(required = false) ProductBrand product_brand,
                                       @RequestParam(required = false) Double price,
                                       @RequestParam(required = false) Double electricity_consumption) throws SQLException {
        FilterComponents filterComponents = new FilterComponents(price, category_type, product_brand, product_name, electricity_consumption);
        return productService.filterProducts(filterComponents);
    }
}