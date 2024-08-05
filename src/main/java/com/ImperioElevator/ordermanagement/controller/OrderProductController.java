package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.service.SaveOrderProductService;
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
}
