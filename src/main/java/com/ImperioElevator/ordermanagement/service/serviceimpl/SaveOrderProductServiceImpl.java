package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.OrderProductDao;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.service.SaveOrderProductService;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class SaveOrderProductServiceImpl implements SaveOrderProductService {

    private final OrderProductDao orderProductDao;

    @Autowired
    public SaveOrderProductServiceImpl(OrderProductDao orderProductDao) {
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
    public Long orderProductExtraProduct(OrderProduct orderProduct) throws SQLException {
        return orderProductDao.insert(orderProduct);
    }
}
