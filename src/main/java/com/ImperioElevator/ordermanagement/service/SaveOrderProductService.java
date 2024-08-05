package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;

import java.sql.SQLException;
import java.util.List;

public interface SaveOrderProductService {

     Long saveOrderProducts( List<OrderProduct> orderProducts) throws SQLException;
}
