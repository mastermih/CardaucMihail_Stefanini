package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.service.SaveOrderProductService;
import com.ImperioElevator.ordermanagement.valueobjects.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping()
public class OrderProductController {

    @Autowired
    private final SaveOrderProductService saveOrderProductService;

    public OrderProductController(SaveOrderProductService saveOrderProductService) {
        this.saveOrderProductService = saveOrderProductService;
    }

    @PostMapping("/MakeOrder/ProductOrder")
    public Long addOrderProducts(@RequestBody List<OrderProduct> orderProducts) throws SQLException {
        return saveOrderProductService.saveOrderProducts(orderProducts);
    }

    @GetMapping("/orderProduct")
    public List<OrderProduct> getLastCreatedDate(@RequestParam("limit") Number limit) throws SQLException {
        return saveOrderProductService.getFirstPageOrderProduct(limit);
    }

    @GetMapping("/orderProduct/price")
    public Paginable<OrderProduct> listOrderProductByPrice(@RequestParam Double startPrice,
                                                           @RequestParam Double endPrice,
                                                           @RequestParam Long page,
                                                           @RequestParam Long totalOrderProducts) throws SQLException{
        return saveOrderProductService.findPaginableOrderProductByPriceProduct(startPrice, endPrice, page, totalOrderProducts);
    }
}