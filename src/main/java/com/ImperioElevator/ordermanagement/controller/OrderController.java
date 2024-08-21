package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import com.ImperioElevator.ordermanagement.service.SaveOrderProductService;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping()
public class OrderController {

 private final OrdersService ordersService;
 private final EmailService emailService;

 @Autowired
 public OrderController(OrdersService ordersService, EmailService emailService) {
  this.ordersService = ordersService;
  this.emailService = emailService;
 }

 @GetMapping("/orders/createDate")
 public Paginable<Order> listOrdersByPeriod(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                            @RequestParam Long numberOfOrders,
                                            @RequestParam Long page) throws SQLException {
  LocalDateTime startDateTime = startDate.atStartOfDay();
  LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
  return ordersService.findPaginableOrderByCreatedDate(startDateTime, endDateTime, numberOfOrders, page);
 }

 @GetMapping("/orders/status-createDate")
 public Paginable<Order> listOrdersByPeriodStatus(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                  @RequestParam Status status,
                                                  @RequestParam Long numberOfOrders,
                                                  @RequestParam Long page) throws SQLException {
  LocalDateTime startDateTime = startDate.atStartOfDay();
  LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
  return ordersService.findPaginableOrderByCreatedDateAndStatus(startDateTime, endDateTime, status, numberOfOrders, page);
 }

 @GetMapping("/orders/lastCreated")
 public List<Order> getLastCreatedDate(@RequestParam("limit") Number limit) throws SQLException {
  return ordersService.findLastCreatedOrders(limit);
 }


 @PostMapping("/MakeOrder/{id}")
 public Long postUserOrder(@PathVariable Long id, @RequestBody Order order) throws SQLException {
  return ordersService.createOrder(order);
 }


 @PutMapping("/MakeOrder/{id}")
 public Long updateUserOrder(@PathVariable Long id, @RequestBody Order order) throws SQLException{
  return ordersService.updateOrderStatus(order);
 }
// the Email stuf

 @PostMapping("/sendMail")
public String sendMail(@RequestBody EmailDetails details) {
 return emailService.sendSimpleMail(details);
}
 @PostMapping("/sendMail/OrderId")
 public String sendMailOrderId(@RequestBody EmailDetails details, Long orderId) {
  return emailService.sendConfirmationMail(details, orderId);
 }
 @PostMapping("/sendMail/confirm/{id}")
 public Long sendConfirmationOrderEmailStatus(@RequestBody Id id) throws SQLException {
  return emailService.updateOrderEmailConfirmStatus(id.id());
 }
 @GetMapping("/sendMail/confirm/{id}")
 public Order getOrderIdConfirmEmail(@PathVariable("id") Long id) throws SQLException{
  return null;
 }

}
