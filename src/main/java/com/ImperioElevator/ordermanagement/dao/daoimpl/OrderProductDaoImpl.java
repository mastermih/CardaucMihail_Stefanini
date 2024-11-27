package com.ImperioElevator.ordermanagement.dao.daoimpl;

import com.ImperioElevator.ordermanagement.dao.OrderProductDao;
import com.ImperioElevator.ordermanagement.entity.*;
import com.ImperioElevator.ordermanagement.valueobjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.Number;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderProductDaoImpl extends AbstractDao<OrderProduct> implements OrderProductDao {
    //fiend orderporuduct where by user id  then order id where the order_status will be IN_PROGRESS
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(OrderProductDaoImpl.class);

    public OrderProductDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(OrderProduct orderProduct) throws SQLException {
        String sql = "INSERT INTO order_product (order_id, product_name, quantity, price_product, discount_percentages, price_discount, VAT, price_with_VAT, parent_product_id, product_id) VALUES (?, ?, ?, ?, ?, ?, ?, ? ,?, ?)";

        // Default values
        long VAT = 20L;
        long discountPercentages = 0L;

        BigDecimal priceProduct = BigDecimal.valueOf(orderProduct.priceOrder().price());

        double priceDiscount = orderProduct.product().price().price();

        double priceWithVAT = priceDiscount + (priceDiscount * VAT / 100);


        Long parentId = orderProduct.parentProductId() != null ? orderProduct.parentProductId().id() : null;
        String productName = orderProduct.product().productName().productName();

        try {
            logger.debug("Executing SQL for order product insert: {}", sql);

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setLong(1, orderProduct.order().orderId().id());
                ps.setString(2, productName);
                ps.setInt(3, orderProduct.quantity().quantity());
                ps.setBigDecimal(4, BigDecimal.valueOf(orderProduct.priceOrder().price()));
                ps.setLong(5, orderProduct.discount_percentages().discount_percentages());
                ps.setDouble(6, orderProduct.price_discount().price());
                ps.setLong(7, orderProduct.VAT().VAT());
                ps.setDouble(8, orderProduct.price_with_VAT().price());
                if (parentId != null) {
                    ps.setLong(9, parentId);
                } else {
                    ps.setNull(9, Types.BIGINT);
                }
                ps.setBigDecimal(10, BigDecimal.valueOf(orderProduct.product().productId().id()));
                return ps;
            });

            logger.info("Successfully inserted OrderProduct with orderId: {}", orderProduct.order().orderId().id());

            return orderProduct.order().orderId().id();

        } catch (DataAccessException ex) {
            logger.error("Failed to insert OrderProduct with orderId: {}", orderProduct.order().orderId().id(), ex);
            throw ex;
        }
    }

    @Override
    public Long update(OrderProduct orderProduct) throws SQLException {
        String sql = "UPDATE order_product SET quantity = ?, price_product = ?, product_id = ? WHERE order_id = ? AND product_name = ?";
        String productName = orderProduct.product().productName().productName();

        try {
            logger.debug("Executing SQL for order product update: {}", sql);

            jdbcTemplate.update(sql,
                    orderProduct.quantity().quantity(),
                    orderProduct.priceOrder().price(),
                    orderProduct.product().productId().id(),
                    orderProduct.order().orderId().id(),
                    productName);

            logger.info("Successfully updated OrderProduct with orderId: {}", orderProduct.order().orderId().id());

            return orderProduct.order().orderId().id();

        } catch (DataAccessException ex) {
            logger.error("Failed to update OrderProduct with orderId: {}", orderProduct.order().orderId().id(), ex);
            throw ex;
        }
    }

    @Override
    public Long deleteByIdAndName(Long orderId, String productName) throws SQLException {
        String sql = "DELETE FROM order_product WHERE order_id = ? AND product_name = ? LIMIT 1";

        try {
            logger.debug("Executing SQL to delete OrderProduct: {}", sql);

            jdbcTemplate.update(sql, orderId, productName);

            logger.info("Successfully deleted OrderProduct with orderId: {} and productName: {}", orderId, productName);

            return orderId;

        } catch (DataAccessException ex) {
            logger.error("Failed to delete OrderProduct with orderId: {} and productName: {}", orderId, productName, ex);
            throw ex;
        }
    }

    //This is the method for the sql fille
    @Override
    public List<OrderProduct> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT op.* FROM order_product " +
                    "JOIN orders o ON o.id = user_id " +
                     "WHERE o.order_status = 'IN_PROGRESS' " +
                     "ORDER BY o.id ASC LIMIT 1";
        try{
            logger.debug("Executing the SQL to fiend the order product By userId ", sql );
            List<OrderProduct> orderProducts = new ArrayList<>();
            jdbcTemplate.query(sql, new Object[]{userId}, result ->{
                orderProducts.add(mapResultSetToEntity(result));
            });
            return orderProducts;
        }catch (DataAccessException ex){
            logger.error("Failed to fiend OrderProduct with userId: {}", userId, ex);
            throw ex;
        }
    }

    @Override
    public OrderProduct findByIdAndName(Long orderId, String productName) throws SQLException {
        String sql = "SELECT * FROM order_product WHERE order_id = ? AND product_name = ?";

        try {
            logger.debug("Executing SQL to find OrderProduct by id and name: {}", sql);

            return jdbcTemplate.queryForObject(sql, new Object[]{orderId, productName}, (resultSet, i) -> mapResultSetToEntity(resultSet));

        } catch (EmptyResultDataAccessException e) {
            logger.error("No OrderProduct found with orderId: {} and productName: {}", orderId, productName, e);
            return null;
        } catch (DataAccessException ex) {
            logger.error("Failed to find OrderProduct with orderId: {} and productName: {}", orderId, productName, ex);
            throw ex;
        }
    }

    @Override
    public List<OrderProduct> findLastCreatedOrderProducts(Number limit) throws SQLException {
        String sql = "SELECT * FROM order_product ORDER BY order_id ASC LIMIT ?";
        List<OrderProduct> orderProducts = new ArrayList<>();

        try {
            logger.debug("Executing SQL to find last created OrderProducts: {}", sql);

            jdbcTemplate.query(sql, new Object[]{limit}, resultSet -> {
                orderProducts.add(mapResultSetToEntity(resultSet));
            });

            logger.info("Successfully retrieved last created OrderProducts");

            return orderProducts;

        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve last created OrderProducts", ex);
            throw ex;
        }
    }

    @Override
    public Paginable<OrderProduct> finedPaginableOrderProductByProductPice(Double startPrice, Double endPrice, Long page, Long numberOfOrderProducts) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM order_product WHERE price_product BETWEEN ? AND ?";
        String sql = "SELECT * FROM order_product WHERE price_product BETWEEN ? AND ? LIMIT ? OFFSET ?";
        List<OrderProduct> orderProducts = new ArrayList<>();
        Long offset = (page - 1) * numberOfOrderProducts;

        try {
            logger.debug("Executing SQL to find paginable OrderProducts by price: {}", sql);

            Long totalItems = jdbcTemplate.queryForObject(countSql, new Object[] {(startPrice), (endPrice)}, Long.class);

            jdbcTemplate.query(sql, new Object[]{startPrice, endPrice, numberOfOrderProducts, offset}, resultSet -> {
                orderProducts.add(mapResultSetToEntity(resultSet));
            });

            Long totalPages = (long) Math.ceil((double) totalItems / numberOfOrderProducts);

            logger.info("Successfully retrieved paginable OrderProducts by price");

            return new Paginable<>(orderProducts, page, totalPages);

        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve paginable OrderProducts by price", ex);
            throw ex;
        }
    }

    @Override
    public List<OrderProduct> findByOrderId(Long orderId) throws SQLException {
        String sql = "SELECT * FROM order_product WHERE order_id = ?";

        try {
            logger.debug("Executing SQL to find OrderProducts by orderId: {}", sql);

            List<OrderProduct> orderProducts = jdbcTemplate.query(sql, new Object[]{orderId}, (resultSet, i) -> mapResultSetToEntity(resultSet));

            logger.info("Successfully retrieved OrderProducts for orderId: {}", orderId);

            return orderProducts;

        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve OrderProducts for orderId: {}", orderId, ex);
            throw ex;
        }
    }

    @Override
    public OrderProduct mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Long orderId = resultSet.getLong("order_id");
        int quantity = resultSet.getInt("quantity");
        double priceOrder = resultSet.getInt("price_product");
        String productName = resultSet.getString("product_name");
        Long parentId = resultSet.getLong("parent_product_id");
        Long productId = resultSet.getLong("product_id");
        Long discount_percentages = resultSet.getLong("discount_percentages");
        double price_discount = resultSet.getDouble("price_discount");
        Long VAT = resultSet.getLong("VAT");
        double price_with_VAT =  resultSet.getDouble("price_with_VAT");

        // Create and return a new OrderProduct object
        return new OrderProduct(
                new Id(null),
                new Order(new Id(orderId), null, null, null, null, null),
                new Quantity(quantity),
                new Price(priceOrder),
                new DiscountPercentages(discount_percentages),
                new Price(price_discount),
                new Vat(VAT),
                new Price(price_with_VAT),
                new Id(parentId),
                new Product(new Id(productId), null, null, null, null, null, null, new ProductName(productName), null, null, null, null)
        );
    }
}