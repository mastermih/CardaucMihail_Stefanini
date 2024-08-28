package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.dto.OrderWithProductsDTO;
import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import com.ImperioElevator.ordermanagement.service.OrderProductService;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import liquibase.pro.packaged.S;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 private final OrderProductService orderProductService;

 @Autowired
 public OrderController(OrdersService ordersService, EmailService emailService, OrderProductService orderProductService) {
  this.ordersService = ordersService;
  this.emailService = emailService;
  this.orderProductService = orderProductService;
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

 @PutMapping("/MakeOrder/{id}")
 public Long updateUserOrder(@PathVariable Long id, @RequestBody Order order) throws SQLException{
  return ordersService.updateOrderStatus(order);
 }



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

 @GetMapping("/orderProduct")

 public List<OrderProduct> getLastCreatedDateOrderProduct(@RequestParam("limit") Number limit) throws SQLException {
  return orderProductService.getFirstPageOrderProduct(limit);
 }

 @GetMapping("/orderProduct/price")
 public Paginable<OrderProduct> listOrderProductByPrice(@RequestParam Double startPrice,
                                                        @RequestParam Double endPrice,
                                                        @RequestParam Long page,
                                                        @RequestParam Long totalOrderProducts) throws SQLException{
  return orderProductService.findPaginableOrderProductByPriceProduct(startPrice, endPrice, page, totalOrderProducts);
 }


 @PostMapping("/MakeOrder")
 public ResponseEntity<Long> createOrderWithProducts(@RequestBody OrderWithProductsDTO orderWithProductsDTO) throws SQLException {
  Order order = orderWithProductsDTO.getOrder();
  List<OrderProduct> orderProducts = orderWithProductsDTO.getOrderProducts();

  Long orderId = ordersService.createOrderWithProducts(order, orderProducts);

  HttpHeaders headers = new HttpHeaders();
  headers.add("Order-Creation-Message", "Order and associated products were successfully created.");
  headers.add("Number-Of-Products", String.valueOf(orderProducts.size()));

   return new ResponseEntity<>(orderId, headers, HttpStatus.CREATED);
  }


 @PostMapping("/MakeOrder/ProductOrder")
 public Long orderProductExtraProduct (@RequestBody OrderProduct orderProduct) throws SQLException{
  return orderProductService.orderProductInsertExtraProduct(orderProduct);
 }



 // Get the orderProduct
 @GetMapping("/MakeOrder/{id}")
 public List<Object[]> getOrderWithExtraProducts(@PathVariable("id") Long orderId) throws SQLException {
  return ordersService.getOrderWithExtraProducts(orderId);
 }

 @DeleteMapping("/MakeOrder/ProductOrder")
 public Long deleteOrderProductExtraProduct(@RequestParam("id") Long orderId,
                                            @RequestParam("product_name") String productName) throws SQLException{
  return orderProductService.deleteOrderProductExtraProduct(orderId, productName);
 }
}
