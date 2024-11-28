package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.dto.EntityCreationResponse;
import com.ImperioElevator.ordermanagement.dto.OrderWithProductsDTO;
import com.ImperioElevator.ordermanagement.dto.OrdersFoundLastCreatedDTO;
import com.ImperioElevator.ordermanagement.entity.*;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import com.ImperioElevator.ordermanagement.service.OrderProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController

public class OrderController {

 private final OrdersService ordersService;
 private final EmailService emailService;
 private final OrderProductService orderProductService;

 public OrderController(OrdersService ordersService, EmailService emailService, OrderProductService orderProductService) {
  this.ordersService = ordersService;
  this.emailService = emailService;
  this.orderProductService = orderProductService;
 }

 private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

 @GetMapping("/orders/createDate")
 public Paginable<OrdersFoundLastCreatedDTO> listOrdersByPeriod(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                            @RequestParam Long numberOfOrders,
                                            @RequestParam Long page) throws SQLException {
  LocalDateTime startDateTime = startDate.atStartOfDay();
  LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
  return ordersService.findPaginableOrderByCreatedDate(startDateTime, endDateTime, numberOfOrders, page);
 }

 @GetMapping("/userOrders/createDate")
 public Paginable<Order> listUserOrdersByPeriod(@RequestParam Long id,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                            @RequestParam Long numberOfOrders,
                                            @RequestParam Long page) throws SQLException {
  LocalDateTime startDateTime = startDate.atStartOfDay();
  LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
  return ordersService.findPaginableUserOrderByCreatedDate(id,startDateTime, endDateTime, numberOfOrders, page);
 }

 @GetMapping("/orders/status-createDate")
 public Paginable<OrdersFoundLastCreatedDTO> listOrdersByPeriodStatus(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                  @RequestParam Status status,
                                                  @RequestParam Long numberOfOrders,
                                                  @RequestParam Long page) throws SQLException {
  LocalDateTime startDateTime = startDate.atStartOfDay();
  LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
  return ordersService.findPaginableOrderByCreatedDateAndStatus(startDateTime, endDateTime, status, numberOfOrders, page);
 }


 @GetMapping("/orders/lastCreated")
 public List<OrdersFoundLastCreatedDTO> getLastCreatedDate(@RequestParam("limit") Number limit) throws SQLException {
  return ordersService.findLastCreatedOrders(limit);
 }
 @GetMapping("/userOrders/UserLastCreated")
 public List<Order> getLastCreatedDateUser(@RequestParam("limit") Number limit,
                                           @RequestParam("id") Long id) throws SQLException {
  return ordersService.findLastCreatedOrdersForUserRole(limit, id);
 }

 //Confirm The Order + OrderProduct Price (Confirm Order) + Email
 @PutMapping("/MakeOrder/{id}")
 public Long updateUserOrder(@PathVariable Long id, @RequestBody Order order) throws SQLException{
  return ordersService.updateOrderStatus(order);
 }

 // The confirmation that user send (From email link)
 @PostMapping("/sendMail/confirm/{token}")
 public String sendConfirmationOrderEmailStatus(@PathVariable("token") String token) throws SQLException {
  logger.debug("Received token: {}", token);
  return emailService.updateOrderEmailConfirmStatus(token);
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


 //Creation of The Order + OrderProduct the initialization /1
 @PostMapping("/MakeOrder")
 public ResponseEntity<EntityCreationResponse> createOrderWithProducts(@RequestBody OrderWithProductsDTO orderWithProductsDTO) throws SQLException {

  Order order = orderWithProductsDTO.getOrder();
  List<OrderProduct> orderProducts = orderWithProductsDTO.getOrderProducts();
  Long orderId = ordersService.createOrderWithProducts(order, orderProducts);

  EntityCreationResponse response = new EntityCreationResponse(
          orderId,
          "Order and associated products were successfully created."
  );

  return new ResponseEntity<>(response, HttpStatus.CREATED);
 }

 // Create just the orderProduct after the Order and first OrderProduct was created /2
 @PostMapping("/MakeOrder/ProductOrder")
 public ResponseEntity<EntityCreationResponse> orderProductExtraProduct (@RequestBody OrderProduct orderProduct) throws SQLException{

  Long orderId = orderProductService.orderProductInsertExtraProduct(orderProduct);
  EntityCreationResponse response = new EntityCreationResponse(
          orderId,
          "Extra Product was added"
  );
  return new ResponseEntity<>(response, HttpStatus.CREATED);
 }

 //The double select method that take the order and the order product with join product table
 @GetMapping("/MakeOrder/{id}")
 public Order getOrderWithExtraProducts(@PathVariable("id") Long orderId) throws SQLException {
  return ordersService.getOrderWithExtraProducts(orderId);
 }

 @DeleteMapping("/MakeOrder/ProductOrder")
 public ResponseEntity<EntityCreationResponse> deleteOrderProductExtraProduct(@RequestParam("id") Long orderId,
                                            @RequestParam("product_name") String productName) throws SQLException{
  Long extraProductId = orderProductService.deleteOrderProductExtraProduct(orderId, productName);
  EntityCreationResponse response = new EntityCreationResponse(
          extraProductId,
          "Was deleted successfully"
  );
  return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
 }

 @GetMapping("orders/assignation")
 public List<String> getOperatorNameToOrder (@RequestParam String name) throws SQLException {
  return ordersService.finedOperatorByName(name);
 }

 //Assigning User(Management) to a User Order
 @PostMapping("orders/assignation")
 public ResponseEntity<EntityCreationResponse> assigneeOperatorToOrder(@RequestParam Long id,
                                                                             @RequestParam String name) throws SQLException{
  ordersService.assigneeOperatorToOrder(id, name);
  EntityCreationResponse entityCreationResponse = new EntityCreationResponse(
          id,
          "Operator " + name + " with id " + id + " assigned to the order"
  );
  return new ResponseEntity<>(entityCreationResponse, HttpStatus.OK);
 }

 @GetMapping("DetailedOrder")
 public List<String> getOperatorAssignedToOrder(@RequestParam Long orderId) throws SQLException{
    return ordersService.getOperatorAssignedToOrder(orderId);
 }

 @DeleteMapping("orders/removeOperator")
 public Long deleteOperatorAssignedToOrderByOperatorId (@RequestParam Long orderId,
                                                        @RequestParam Long operatorId) throws SQLException{
  return ordersService.deleteOperatorAssignedToOrderByOperatorId(orderId, operatorId);
 }

 @DeleteMapping("orders/removeAllOperators")
 public Long deleteAllOperatorsAssignedToOrderByOperatorId (@RequestParam Long orderId)throws SQLException{
  return ordersService.deleteAllOperatorsAssignedToOrderByOperatorId(orderId);
 }

 @PostMapping("orders/assineOrderToMe")
 public Long assineOrderToMe(@RequestParam Long orderId, @RequestParam Long operatorId) throws SQLException {
 return ordersService.assineOrderToMe(orderId, operatorId);
 }

 @PostMapping("orders/orderReadyToPay")
 public Long updateOrderStatusReadyForPayment(@RequestParam  Long orderId,
                                              @RequestHeader("Authorization") String authorizationHeader) throws SQLException{
  String jwtToken = authorizationHeader.replace("Bearer ", "");
  return ordersService.updateOrderStatusReadyForPayment(orderId, jwtToken);
 }
}