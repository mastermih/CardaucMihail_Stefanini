package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.valueobjects.Price;
import liquibase.sql.Sql;

import java.sql.SQLException;
import java.util.List;

public interface SaveOrderProductService {

   //  Long saveOrderProducts(List<OrderProduct> orderProducts) throws SQLException;
     List<OrderProduct> getFirstPageOrderProduct (Number number) throws  SQLException;
     Paginable<OrderProduct> findPaginableOrderProductByPriceProduct (Double startPrice, Double endPrice, Long page, Long numberOfOrderProdcuts)  throws SQLException;
     Long updateOrderProucts(OrderProduct orderProduct) throws SQLException;
     Long orderProductExtraProduct(OrderProduct orderProduct) throws SQLException;
}
