package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

 private final OrdersService ordersService;

 @Autowired
 public OrderController(OrdersService ordersService) {
  this.ordersService = ordersService;
 }

 @CrossOrigin(origins = "http://localhost:3000")
 @GetMapping("/createDate")
 public Paginable<Order> listOrdersByPeriod(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                            @RequestParam Long numberOfOrders,
                                            @RequestParam Long page) throws SQLException {
  LocalDateTime startDateTime = startDate.atStartOfDay();
  LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
  return ordersService.findPaginableOrderByCreatedDate(startDateTime, endDateTime, numberOfOrders, page);
 }

 @CrossOrigin(origins = "http://localhost:3000")
 @GetMapping("/status-createDate")
 public Paginable<Order> listOrdersByPeriodStatus(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                  @RequestParam Status status,
                                                  @RequestParam Long numberOfOrders,
                                                  @RequestParam Long page) throws SQLException {
  LocalDateTime startDateTime = startDate.atStartOfDay();
  LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
  return ordersService.findPaginableOrderByCreatedDateAndStatus(startDateTime, endDateTime, status, numberOfOrders, page);
 }

 @CrossOrigin(origins = "http://localhost:3000")
 @GetMapping("/lastCreated")
 public List<Order> getLastCreatedDate(@RequestParam("limit") Number limit) throws SQLException {
  return ordersService.findLastCreatedOrders(limit);
 }

}
