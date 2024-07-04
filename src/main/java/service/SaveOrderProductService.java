package service;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import daoimplementation.OrderDaoImpl;
import daoimplementation.OrderProductDaoImpl;
import dto.OrderProductDTO;
import dto.UserOrderDTO;
import entity.Order;
import entity.OrderProduct;
import entity.Product;
import entity.User;
import enumobects.Status;
import valueobjects.*;

public class SaveOrderProductService {
    //vine un obiect care se va salva in db de la cliend dupa ce face submit
    //statului il definm in service layer
    //se salveaza mai intii in order
    // apoi ducem inservice unde operam si trimitem in order_product
    // Vine spre salvare un obiect mai mare
    // in service call la sava order_product realtionship
    // idul si statusul il prdefinim in service laier
    // dupa ce din service trimitem in order ne intoarcem inapoi in service si trimitem in order product
    // We will get fro the user UserOrder(user_id, <pentru order>) (product_id, quantity, price_product <pentru category order>)
    private final OrderDaoImpl orderDaoImpl;
    private final OrderProductDaoImpl orderProductDaoImpl;

    public SaveOrderProductService(Connection connection) {
        this.orderDaoImpl = new OrderDaoImpl(connection);
        this.orderProductDaoImpl = new OrderProductDaoImpl(connection);
    }

    public long saveOrderWithProduct(UserOrderDTO userOrderDTO) throws SQLException {
        Order order = convertToOrder(userOrderDTO);
        long orderId = orderDaoImpl.insert(order);
        order.setOrderId(new Id(orderId));
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
                            null, null, null, null, null, null),
                    new Quantity(orderProducts.getQuantity().getQuantity()),
                    new Price(orderProducts.getPriceOrder().getPrice())
            ));

        }
        return orderProduct;
    }
}


