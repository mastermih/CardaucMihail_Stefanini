package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

// Citesc articolul
@RestController
@RequestMapping("/orders")
public class OrderController {
 @Autowired
 private final OrdersService ordersService;

 @Autowired
  public OrderController(OrdersService ordersService){
   this.ordersService = ordersService;
  }
 // Data format per toata aplicatia
 // Jackson() OBJECT MAPER /cu DTO?
 @CrossOrigin(origins = "http://localhost:3000")
 @GetMapping("/createDate")
 public Paginable<Order> listOrdersByPeriod(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                            @RequestParam Long numberOfOrders,
                                            @RequestParam Long page) throws SQLException {
   return ordersService.findPaginableOrderByCreatedDate(startDate, endDate,  numberOfOrders, page);
  }

 @CrossOrigin(origins = "http://localhost:3000") //CORS
 @GetMapping("/status-createDate")
 public Paginable<Order> listOrdersByPeriodStatus(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                  @RequestParam Status status,
                                                  @RequestParam Long numberOfOrders,
                                                  @RequestParam Long page) throws SQLException {
  return ordersService.findPaginableOrderByCreatedDateAndStatus(startDate, endDate, status, numberOfOrders, page);
 }

 @CrossOrigin(origins = "http://localhost:3000")
 @GetMapping("/lastCreated")
 public List<Order> getLastCreatedDate(@RequestParam("limit") Number limit) throws SQLException {
  return ordersService.findLastCreatedOrders(limit);
 }
}
