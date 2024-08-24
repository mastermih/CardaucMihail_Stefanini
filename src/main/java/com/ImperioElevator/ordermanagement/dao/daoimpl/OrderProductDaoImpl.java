package com.ImperioElevator.ordermanagement.dao.daoimpl;

import com.ImperioElevator.ordermanagement.dao.OrderProductDao;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Price;
import com.ImperioElevator.ordermanagement.valueobjects.ProductName;
import com.ImperioElevator.ordermanagement.valueobjects.Quantity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderProductDaoImpl extends AbstractDao<OrderProduct> implements OrderProductDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderProductDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(OrderProduct orderProduct) throws SQLException {
        String sql = "INSERT INTO order_product (order_id, product_name, quantity, price_product, parent_product_id, product_id) VALUES (?, ?, ?, ?, ?, ?)";

        Long parentId = orderProduct.parentProductId() != null ? orderProduct.parentProductId().id() : null;
       // Long productId = orderProduct.product().productId() != null ? orderProduct.product().productId().id() : null;
        String productName = orderProduct.product().productName().productName();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, orderProduct.order().orderId().id());
            ps.setString(2, productName);
            ps.setInt(3, orderProduct.quantity().quantity());
            ps.setBigDecimal(4, BigDecimal.valueOf(orderProduct.priceOrder().price()));

            if (parentId != null) {
                ps.setLong(5, parentId);
            } else {
                ps.setNull(5, Types.BIGINT);
            }
            ps.setBigDecimal(6, BigDecimal.valueOf(orderProduct.product().productId().id()));
            return ps;
        });

        return orderProduct.order().orderId().id();
    }


    @Override
    public Long update(OrderProduct orderProduct) throws SQLException {
        String sql = "UPDATE order_product SET quantity = ?, price_product = ? product_id WHERE order_id = ? AND product_name = ?";
        String productName = orderProduct.product().productName().productName();
        jdbcTemplate.update(sql,
                orderProduct.quantity().quantity(),
                orderProduct.priceOrder().price(),
                orderProduct.product().productId().id(),
                orderProduct.order().orderId().id(),

                productName);
        return orderProduct.order().orderId().id();
    }

    @Override
    public Long deleteById(Long orderId, String productName) throws SQLException {
        String sql = "DELETE FROM order_product WHERE order_id = ? AND product_name = ?";
        jdbcTemplate.update(sql, orderId, productName);
        return orderId;
    }

    @Override
    public OrderProduct findById(Long orderId, String productName) throws SQLException {
        String sql = "SELECT * FROM order_product WHERE order_id = ? AND product_name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{orderId, productName}, (resultSet, i) -> mapResultSetToEntity(resultSet));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<OrderProduct> findLastCreatedOrderProducts(Number limit) throws SQLException {
        String sql = "SELECT * FROM order_product ORDER BY order_id ASC LIMIT ?";
        List<OrderProduct> orderProducts = new ArrayList<>();
        jdbcTemplate.query(sql, new Object[]{limit}, resultSet -> {
            orderProducts.add(mapResultSetToEntity(resultSet));
        });
        return orderProducts;
    }

    @Override
    public Paginable<OrderProduct> finedPaginableOrderProductByProductPice(Double startPrice, Double endPrice, Long page, Long numberOfOrderProducts) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM order_product WHERE price_product BETWEEN ? AND ?";
        String sql = "SELECT * FROM order_product WHERE price_product BETWEEN ? AND ? LIMIT ? OFFSET ?";
        List<OrderProduct> orderProducts = new ArrayList<>();
        Long offset = (page - 1) * numberOfOrderProducts;

        Long totalItems = jdbcTemplate.queryForObject(countSql, new Object[] {(startPrice), (endPrice)}, Long.class);

        jdbcTemplate.query(sql, new Object[]{startPrice, endPrice, numberOfOrderProducts, offset}, resultSet -> {
            orderProducts.add(mapResultSetToEntity(resultSet));
        });

        Long totalPages = (long) Math.ceil((double) totalItems / numberOfOrderProducts);
        return new Paginable<>(orderProducts, page, totalPages);
    }


    @Override
    public OrderProduct mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Long orderId = resultSet.getLong("order_id");
        int quantity = resultSet.getInt("quantity");
        int priceOrder = resultSet.getInt("price_product");
        String productName = resultSet.getString("product_name");
        Long parentId = resultSet.getLong("parent_product_id");
        Long productId = resultSet.getLong("product_id");





        // Create and return a new OrderProduct object
        return new OrderProduct(
                new Id(null),
                new Order(new Id(orderId), null, null, null, null, null),
                new Quantity(quantity),
                new Price(priceOrder),
                new Id(parentId),
                new Product(new Id(productId), null, null, null, null, null, null, new ProductName(productName), null, null, null, null)

                );
    }
}