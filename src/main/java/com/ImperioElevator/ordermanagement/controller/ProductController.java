package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.service.ProductService;
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/catalog")
    public List<Product> getFiendProductForMainPage(@RequestParam ("limit") Long limit,
                                                    @RequestParam("categoryId") Long categoryId) throws SQLException {
        return  productService.fiendProductForMainPage(limit, categoryId);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/product/{id}")
    public Product getFindProductById(@PathVariable("id")Long id) throws SQLException{
        return productService.findProductById(id);
    }
}
