package com.ImperioElevator.ordermanagement.dao.daoimpl;

import com.ImperioElevator.ordermanagement.dao.OrderDao;
import com.ImperioElevator.ordermanagement.entity.*;
import com.ImperioElevator.ordermanagement.enumobects.CategoryType;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.valueobjects.*;
import liquibase.pro.packaged.I;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.RowMapper;

import java.lang.Number;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class OrderDaoImpl extends AbstractDao<Order> implements OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    @Override
    public Long insert(Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, created_date, updated_date, order_status, confirmation_token) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime currentDateTime = LocalDateTime.now();
       String token = TokenGenerator.generateToken();
        CreateDateTime createdDate = order.createdDate() != null ? order.createdDate() : new CreateDateTime(currentDateTime);
        UpdateDateTime updatedDate = order.updatedDate() != null ? order.updatedDate() : new UpdateDateTime(currentDateTime);

        try {
            logger.debug("Executing creation of the Order: {}", sql);  // Log the SQL query

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, order.userId().userId().id());
                ps.setTimestamp(2, Timestamp.valueOf(createdDate.createDateTime()));
                ps.setTimestamp(3, Timestamp.valueOf(updatedDate.updateDateTime()));
                ps.setString(4, order.orderStatus().name());
                ps.setString(5, token);
                return ps;
            }, keyHolder);
            logger.info("Successfully inserted Order for userId: {}", order.userId().userId().id());

            if (keyHolder.getKey() != null) {
                return keyHolder.getKey().longValue();
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        } catch (SQLException ex) {
            logger.error("Failed to insert Order for userId: {}", order.userId().userId().id(), ex);
            throw ex;
        }
    }

    @Override
    public Long update(Order order) throws SQLException {
        String sql = "UPDATE orders SET user_id = ?, updated_date = ?, order_status = ? WHERE id = ?";
       try {
           logger.debug("Update the Order: {}", sql);
           jdbcTemplate.update(sql,
                   order.userId().userId().id(),
                   Timestamp.valueOf(order.updatedDate().updateDateTime()),
                   order.orderStatus().toString(),
                   order.orderId().id());
           logger.info("Successfully updated Order with id: {}", order.orderId().id());
           return order.orderId().id();
       }catch (DataAccessException e){
           logger.error("Failed to update Order with id: {}", order.orderId().id(), e);
           throw e;
       }
    }

    @Override
    public Long updateStatus(Order order) throws SQLException {
        String sql = "UPDATE orders SET order_status = ?, updated_date = ? WHERE id = ?";
        try{
            logger.debug("Executing SQL for order status update: {}", sql);

        jdbcTemplate.update(sql,
                order.orderStatus().toString(),
                order.updatedDate() != null ? order.updatedDate().updateDateTime() : new java.sql.Timestamp(System.currentTimeMillis()),
                order.orderId().id());
            logger.info("Successfully updated Order status for id: {}", order.orderId().id());
            return order.orderId().id();
    }  catch (DataAccessException ex) {
        logger.error("Failed to update Order status for id: {}", order.orderId().id(), ex);
        throw ex;
    }
}

    @Override
    public Long updateOrderEmailConfirmStatus(Long id) throws SQLException {
        String sql = "UPDATE orders SET order_status = 'CONFIRMED' WHERE id = ?";
        try {
            logger.debug("Executing SQL to confirm Order status by email: {}", sql);

            jdbcTemplate.update(sql, id);
            return id;

        } catch (DataAccessException ex) {
            logger.error("Failed to confirm Order status by email for id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    public Order getOrderWithExtraProducts(Long orderId) {
        String sqlOrderInfo = "SELECT " +
                "    o.id, " +
                "    o.user_id, " +
                "    o.order_status, " +
                "    o.created_date, " +
                "    o.updated_date " +
                "FROM " +
                "    orders o " +
                "WHERE " +
                "    o.id = ?";

        String sqlOrderProducts = "SELECT " +
                "    op.order_id, " +
                "    op.quantity, " +
                "    op.price_product, " +
                "    op.product_id, " +
                "    op.parent_product_id, " +
                "    p.product_name, " +
                "    p.category_type, " +
                "    p.image_path " +
                "FROM " +
                "    order_product op " +
                "JOIN " +
                "    product p " +
                "ON " +
                "    op.product_id = p.id " +
                "WHERE " +
                "    op.order_id = ?";

        try {
            logger.debug("Executing SQL to get Order and its products: {}, {}", sqlOrderInfo, sqlOrderProducts);

            Order order = jdbcTemplate.queryForObject(sqlOrderInfo, new Object[]{orderId}, (resultSet, i) -> {
                return mapResultSetToEntity(resultSet);
            });

            List<OrderProduct> orderProducts = jdbcTemplate.query(sqlOrderProducts, new Object[]{orderId}, this::mapOrderProduct);

            // Attach the list of products to the order
            order.orderProducts().addAll(orderProducts);
            logger.info("Successfully retrieved Order with products for id: {}", orderId);

            return order;

        } catch (EmptyResultDataAccessException e) {
            logger.error("No Order found with id: {}", orderId, e);

            return null;
        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve Order with products for id: {}", orderId, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Integer deleteUnconfirmedOrderByEmail() throws SQLException {
        String sql = "UPDATE orders SET order_status = 'EXPIRED' " +
                "WHERE order_status = 'NEW' " +
                "AND updated_date <= NOW() - INTERVAL 1 DAY;";
        try {
            int updatedRows = jdbcTemplate.update(sql);
            logger.info("Successfully expired {} unconfirmed orders", updatedRows);
            return updatedRows;
        }catch (DataAccessException ex){
            logger.error("Failed to expire unconfirmed orders", ex);
            throw ex;
        }
    }


    @Override
    public Long deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        logger.debug("Deleted Order by id: {}", sql);
        jdbcTemplate.update(sql, id);
        return id;
    }

    @Override
    public Order findById(Long id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            logger.debug("Fiend Order by id: {}", sql);
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> mapResultSetToEntity(resultSet));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Failed to find Order with id: {}", id, e);
            return null;
        }
    }

    @Override
    public Order mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Long orderId = resultSet.getLong("id");
        Long userId = resultSet.getLong("user_id");
        LocalDateTime createdDateTime = resultSet.getTimestamp("created_date").toLocalDateTime();
        LocalDateTime updatedDateTime = resultSet.getTimestamp("updated_date").toLocalDateTime();
        Status orderStatus = Status.valueOf(resultSet.getString("order_status"));
        return new Order(
                new Id(orderId),
                new User(new Id(userId), null, null, null, null, true),
                orderStatus,
                new CreateDateTime(createdDateTime),
                new UpdateDateTime(updatedDateTime),
              new ArrayList<>()
        );
    }
    // ResultSet to  OrderProduct entity
    private OrderProduct mapOrderProduct(ResultSet resultSet, int i) throws SQLException {
        Long parentProductId = resultSet.getLong("parent_product_id");

        return new OrderProduct(
                new Id(resultSet.getLong("product_id")),
                null,  //cyc depend
                new Quantity(resultSet.getInt("quantity")),
                new Price(resultSet.getDouble("price_product")),
                new Id(parentProductId),
                new Product(
                        new Id(resultSet.getLong("product_id")),
                        null, null, null, null, null, null,
                        new ProductName(resultSet.getString("product_name")),
                        null, null,
                        new Image(resultSet.getString("image_path")),
                        CategoryType.valueOf(resultSet.getString("category_type"))
                )
        );
    }

    @Override
    public Paginable<Order> findPaginableOrderByCreatedDate(LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM orders WHERE created_date BETWEEN ? AND ?";
        String sql = "SELECT * FROM orders WHERE created_date BETWEEN ? AND ? LIMIT ? OFFSET ?";
        try {
            logger.debug("Executing SQL to find paginable Orders by created date: {}", sql);

            List<Order> orders = new ArrayList<>();
            Long offset = (page - 1) * numberOfOrders;

            Long totalItems = jdbcTemplate.queryForObject(countSql, new Object[]{Timestamp.valueOf(startDate), Timestamp.valueOf(endDate)}, Long.class);

            jdbcTemplate.query(sql, new Object[]{Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), numberOfOrders, offset}, resultSet -> {
                orders.add(mapResultSetToEntity(resultSet));
            });

            Long totalPages = (long) Math.ceil((double) totalItems / numberOfOrders);
            logger.info("Successfully retrieved paginable Orders by created date");

            return new Paginable<>(orders, page, totalPages);
        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve paginable Orders by created date", ex);
            throw ex;
        }
    }

    @Override
    public Paginable<Order> findPaginableOrderByUpdatedDate(LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM orders WHERE updated_date BETWEEN ? AND ?";
        String sql = "SELECT * FROM orders WHERE updated_date BETWEEN ? AND ? LIMIT ? OFFSET ?";
        try {
            logger.debug("Executing SQL to find paginable Orders by updated date: {}", sql);

            Long totalItems = jdbcTemplate.queryForObject(countSql, new Object[]{Timestamp.valueOf(startDate), Timestamp.valueOf(endDate)}, Long.class);
            List<Order> orders = new ArrayList<>();
            Long offset = (page - 1) * numberOfOrders;

            jdbcTemplate.query(sql, new Object[]{Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), numberOfOrders, offset}, resultSet -> {
                orders.add(mapResultSetToEntity(resultSet));
            });

            Long totalPages = (long) Math.ceil((double) totalItems / numberOfOrders);

            logger.info("Successfully retrieved paginable Orders by updated date");

            return new Paginable<>(orders, page, totalPages);

        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve paginable Orders by updated date", ex);
            throw ex;
        }
    }

    @Override
    public Paginable<Order> findPaginableOrderByCreatedDateAndStatus(LocalDateTime startDate, LocalDateTime endDate, Status status, Long numberOfOrders, Long page) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM orders WHERE created_date BETWEEN ? AND ? AND order_status = ?";
        String sql = "SELECT * FROM orders WHERE created_date BETWEEN ? AND ? AND order_status = ? LIMIT ? OFFSET ?";
        try {
            logger.debug("Executing SQL to find paginable Orders by created date and status: {}", sql);

            Long totalItems = jdbcTemplate.queryForObject(countSql, new Object[]{Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), status.name()}, Long.class);
            List<Order> orders = new ArrayList<>();
            Long offset = (page - 1) * numberOfOrders;

            jdbcTemplate.query(sql, new Object[]{Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), status.name(), numberOfOrders, offset}, resultSet -> {
                orders.add(mapResultSetToEntity(resultSet));
            });

            Long totalPages = (long) Math.ceil((double) totalItems / numberOfOrders);

            logger.info("Successfully retrieved paginable Orders by created date and status");

            return new Paginable<>(orders, page, totalPages);

        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve paginable Orders by created date and status", ex);
            throw ex;
        }
    }

    @Override
    public List<Order> findLastCreatedOrders(Number limit) throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY id ASC LIMIT ?";
        try {
            logger.debug("Executing SQL to find last created Orders: {}", sql);

            List<Order> orders = new ArrayList<>();
            jdbcTemplate.query(sql, new Object[]{limit}, resultSet -> {
                orders.add(mapResultSetToEntity(resultSet));
            });

            logger.info("Successfully retrieved last created Orders");

            return orders;

        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve last created Orders", ex);
            throw ex;
        }
    }
}