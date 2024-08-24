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
//Todo to add in order the logic of orderProduct ...
//Poate va fi sters :)
public class OrderProductController {

    @Autowired
    private final SaveOrderProductService saveOrderProductService;

    public OrderProductController(SaveOrderProductService saveOrderProductService) {
        this.saveOrderProductService = saveOrderProductService;
    }

}