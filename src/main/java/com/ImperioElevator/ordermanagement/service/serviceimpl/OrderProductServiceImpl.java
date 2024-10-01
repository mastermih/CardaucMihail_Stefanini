package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.OrderProductDao;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.service.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductDao orderProductDao;

    @Autowired
    public OrderProductServiceImpl(OrderProductDao orderProductDao) {
        this.orderProductDao = orderProductDao;
    }

    @Override
    public List<OrderProduct> getFirstPageOrderProduct(Number number) throws SQLException {
        return orderProductDao.findLastCreatedOrderProducts(number);
    }

    @Override
    public Paginable<OrderProduct> findPaginableOrderProductByPriceProduct(Double startPrice, Double endPrice, Long page, Long numberOfOrderProdcuts) throws SQLException {
        return orderProductDao.finedPaginableOrderProductByProductPice(startPrice, endPrice, page, numberOfOrderProdcuts);
    }

    @Override
    public Long updateOrderProucts(OrderProduct orderProduct) throws SQLException {
        return orderProductDao.update(orderProduct);
    }

    @Override
    public Long orderProductInsertExtraProduct(OrderProduct orderProduct) throws SQLException {
        return orderProductDao.insert(orderProduct);
    }

    @Override
    public Long deleteOrderProductExtraProduct(Long orderId, String productName) throws SQLException {
        return orderProductDao.deleteByIdAndName(orderId, productName);
    }

}
