package com.ImperioElevator.ordermanagement.dao.daoimpl;

import com.ImperioElevator.ordermanagement.dao.OrderProductDao;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Price;
import com.ImperioElevator.ordermanagement.valueobjects.Quantity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderProductDaoImpl extends AbstractDao<OrderProduct> implements OrderProductDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderProductDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(OrderProduct orderProduct) throws SQLException {
        String sql = "INSERT INTO order_product (order_id, product_id, quantity, price_product) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                orderProduct.order().orderId().id(),
                orderProduct.product().productId().id(),
                orderProduct.quantity().quantity(),
                orderProduct.priceOrder().price());
        return orderProduct.order().orderId().id();
    }

    @Override
    public Long update(OrderProduct orderProduct) throws SQLException {
        String sql = "UPDATE order_product SET quantity = ?, price_product = ? WHERE order_id = ? AND product_id = ?";
        jdbcTemplate.update(sql,
                orderProduct.quantity().quantity(),
                orderProduct.priceOrder().price(),
                orderProduct.order().orderId().id(),
                orderProduct.product().productId().id());
        return orderProduct.order().orderId().id();
    }

    @Override
    public Long deleteById(Long orderId, Long productId) throws SQLException {
        String sql = "DELETE FROM order_product WHERE order_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, orderId, productId);
        return orderId;
    }

    @Override
    public OrderProduct findById(Long orderId, Long productId) throws SQLException {
        String sql = "SELECT * FROM order_product WHERE order_id = ? AND product_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{orderId, productId}, (resultSet, i) -> mapResultSetToEntity(resultSet));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public OrderProduct mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Long orderId = resultSet.getLong("order_id");
        Long productId = resultSet.getLong("product_id");
        int quantity = resultSet.getInt("quantity");
        int priceOrder = resultSet.getInt("price_product");

        // Create and return a new OrderProduct object
        return new OrderProduct(
                new Id(null),
                new Order(new Id(orderId), null, null, null, null, null),
                new Product(new Id(productId), null, null, null, null, null, null, null, null, null, null, null),
                new Quantity(quantity),
                new Price(priceOrder)
        );
    }
}