package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/MakeOrder")
public class OrderUserController {

    private final OrdersService ordersService;

    public OrderUserController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }


    @PostMapping("/{id}")
  public Long postUserOrder(@PathVariable Long id, @RequestBody Order order) throws SQLException {
        return ordersService.createOrder(order);
   }
}
