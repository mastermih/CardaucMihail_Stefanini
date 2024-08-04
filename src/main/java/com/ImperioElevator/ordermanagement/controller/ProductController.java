package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.service.ProductService;
import com.ImperioElevator.ordermanagement.valueobjects.Name;
import com.ImperioElevator.ordermanagement.valueobjects.Number;
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
    public List<Product> getFiendProductForMainPage(@RequestParam ("limit") Long limit,
                                                    @RequestParam("categoryType") String categoryType) throws SQLException {
        return  productService.fiendProductForMainPage(limit, categoryType);
    }

    @GetMapping("/product/{id}")
    public Product getFindProductById(@PathVariable("id")Long id) throws SQLException{
        return productService.findProductById(id);
    }

    @GetMapping("MakeOrder/{id}")
    public List<Product> getFiendProductByName(@PathVariable("id")Long id, @RequestParam("product_name") String name) throws SQLException{
        return productService.findProductByName(name);
    }
}
