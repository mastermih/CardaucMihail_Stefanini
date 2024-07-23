package com.ImperioElevator.ordermanagement.service.serviceimpl;



import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderProductDaoImpl;
import com.ImperioElevator.ordermanagement.dto.OrderProductDTO;
import com.ImperioElevator.ordermanagement.dto.UserOrderDTO;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.valueobjects.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaveOrderProductService {

    private final OrderDaoImpl orderDaoImpl;
    private final OrderProductDaoImpl orderProductDaoImpl;


    public SaveOrderProductService(Connection connection) {
        this.orderDaoImpl = new OrderDaoImpl((JdbcTemplate) connection);
        this.orderProductDaoImpl = new OrderProductDaoImpl((JdbcTemplate) connection);
    }

    public Long saveOrderWithProduct(UserOrderDTO userOrderDTO) throws SQLException {
        Order order = convertToOrder(userOrderDTO);
        Long orderId = orderDaoImpl.insert(order);
        order.setOrderId(new Id(order.getOrderId().getId()));
        List<OrderProduct> orderProducts = convertToOrderProduct(order, userOrderDTO.getOrderProducts());
        for(OrderProduct orderProduct : orderProducts){
            orderProductDaoImpl.insert(orderProduct);
        }
        return orderId;
    }

    public Order convertToOrder(UserOrderDTO userOrderDTO) {
        Order order = new Order(
                null,
                new User(new Id(userOrderDTO.getUserId().getUserId().getId()), null, null),
                Status.NEW,
                new CreateDateTime(userOrderDTO.getCreatedDate().getCreateDateTime()),
                new UpdateDateTime(userOrderDTO.getUpdatedDate().getUpdateDateTime())
        );
        return order;
    }

    public List convertToOrderProduct(Order order, List<OrderProductDTO> orderProductDTOs) {
        List<OrderProduct> orderProduct = new ArrayList<>();
        for (OrderProductDTO orderProducts : orderProductDTOs) {
            orderProduct.add(new OrderProduct(
                    order,
                    new Product(new Id(orderProducts.getPrdoductId().getId()), null, null, null,
                            null, null, null, null, null, null, null),
                    new Quantity(orderProducts.getQuantity().getQuantity()),
                    new Price(orderProducts.getPriceOrder().getPrice())
            ));

        }
        return orderProduct;
    }
}


