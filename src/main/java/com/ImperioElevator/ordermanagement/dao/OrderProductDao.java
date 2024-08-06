package com.ImperioElevator.ordermanagement.dao;


import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;

import java.sql.SQLException;
import java.util.List;

public interface OrderProductDao {
    Long insert(OrderProduct orderProduct) throws SQLException;

    Long update(OrderProduct orderProduct) throws SQLException;

    Long deleteById(Long orderId, Long productId) throws SQLException;

    OrderProduct findById(Long orderId, Long productId) throws SQLException;

    List<OrderProduct> findLastCreatedOrderProducts(Number limit) throws SQLException;

}
