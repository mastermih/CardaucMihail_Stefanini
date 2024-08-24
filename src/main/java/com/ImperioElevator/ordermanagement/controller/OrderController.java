package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.dto.OrderWithProductsDTO;
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
 private final SaveOrderProductService saveOrderProductService;

 @Autowired
 public OrderController(OrdersService ordersService, EmailService emailService, SaveOrderProductService saveOrderProductService) {
  this.ordersService = ordersService;
  this.emailService = emailService;
  this.saveOrderProductService = saveOrderProductService;
 }
//ToDo The productOrder Logic request on back  5 metode   vizualizare, ordere in details, 3 metode pentru submit
 //ToDo OrderPrdcut parent si id trebu cumva in controler de zapihnit, de spus orice logica de genu de pe Ui
 //ToDO rename parent in parentProductId
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


// @PostMapping("/MakeOrder/{id}")
// public Long postUserOrder(@PathVariable Long id, @RequestBody Order order) throws SQLException {
//  //nu uita sa stergi asta
//  System.out.println(order);
//  return ordersService.createOrder(order);
//
// }


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

 //The fost OrderProduct is now here


// @PostMapping("/MakeOrder/ProductOrder")
// public Long addOrderProducts(@RequestBody List<OrderProduct> orderProducts) throws SQLException {
//  return saveOrderProductService.saveOrderProducts(orderProducts);
// }

 @GetMapping("/orderProduct")

 public List<OrderProduct> getLastCreatedDateOrderProduct(@RequestParam("limit") Number limit) throws SQLException {
  return saveOrderProductService.getFirstPageOrderProduct(limit);
 }

 @GetMapping("/orderProduct/price")
 public Paginable<OrderProduct> listOrderProductByPrice(@RequestParam Double startPrice,
                                                        @RequestParam Double endPrice,
                                                        @RequestParam Long page,
                                                        @RequestParam Long totalOrderProducts) throws SQLException{
  return saveOrderProductService.findPaginableOrderProductByPriceProduct(startPrice, endPrice, page, totalOrderProducts);
 }

 //Sub iNtrebare avatiura

 @PostMapping("/MakeOrder")
 public Long createOrderWithProducts(@RequestBody OrderWithProductsDTO orderWithProductsDTO) throws SQLException {
  // Extract order and orderProducts from the DTO
  Order order = orderWithProductsDTO.getOrder();
  List<OrderProduct> orderProducts = orderWithProductsDTO.getOrderProducts();

  // Create the order and the  order_products
  Long orderId = ordersService.createOrderWithProducts(order, orderProducts);

  // Return the generated orderId to the client
  return orderId;
 }

 @PostMapping("/MakeOrder/ProductOrder")
 public Long orderProductExtraProduct (@RequestBody OrderProduct orderProduct) throws SQLException{
  return saveOrderProductService.orderProductExtraProduct(orderProduct);
 }


}
