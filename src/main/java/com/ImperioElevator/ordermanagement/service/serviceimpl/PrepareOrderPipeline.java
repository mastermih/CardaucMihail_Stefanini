package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.ImperioElevator.ordermanagement.enumobects.Status.READY_FOR_PAYMENT;
@Service

public class PrepareOrderPipeline {

    private final PrepareOrderFunction prepareOrderFunction;

    public PrepareOrderPipeline(PrepareOrderFunction prepareOrderFunction) {
        this.prepareOrderFunction = prepareOrderFunction;
    }

    public Order prepareOrder(Long orderId, OrderDaoImpl orderDao) {
        BiFunction<Long, OrderDaoImpl, Order> prepareOrderStep = prepareOrderFunction.prepareOrderFunction.andThen(order -> {
            if (!READY_FOR_PAYMENT.equals(order.orderStatus())) {
                throw new RuntimeException("Order status update failed.");
            }
            return order;
        });

        return prepareOrderStep.apply(orderId, orderDao);
    }
    public List<String> fetchOperators(Long orderId, OrderDaoImpl orderDao) {
        return prepareOrderFunction.fetchOperatorsFunction.apply(orderId, orderDao);
    }
}
