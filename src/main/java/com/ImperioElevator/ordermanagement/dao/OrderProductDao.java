package com.ImperioElevator.ordermanagement.dao;


import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.valueobjects.Price;

import java.sql.SQLException;
import java.util.List;

public interface OrderProductDao {
    Long insert(OrderProduct orderProduct) throws SQLException;

    Long update(OrderProduct orderProduct) throws SQLException;

    Long deleteById(Long orderId, String productName) throws SQLException;

    OrderProduct findByIdAndName(Long orderId, String productName) throws SQLException;

    List<OrderProduct> findLastCreatedOrderProducts(Number limit) throws SQLException;

    Paginable<OrderProduct> finedPaginableOrderProductByProductPice(Double startPrice, Double endPrice, Long numberOfOrderProducts, Long page) throws SQLException;

    List<OrderProduct> findByOrderId(Long orderId) throws SQLException;


}
