package com.ImperioElevator.ordermanagement.service;

import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;

import java.sql.SQLException;
import java.util.List;

public interface OrderProductService {

     List<OrderProduct> getFirstPageOrderProduct (Number number) throws  SQLException;
     Paginable<OrderProduct> findPaginableOrderProductByPriceProduct (Double startPrice, Double endPrice, Long page, Long numberOfOrderProdcuts)  throws SQLException;
     Long updateOrderProucts(OrderProduct orderProduct) throws SQLException;
     Long orderProductInsertExtraProduct(OrderProduct orderProduct) throws SQLException;
     Long deleteOrderProductExtraProduct(Long orderId, String productName) throws SQLException;
}
