package com.ImperioElevator.ordermanagement.dao.daoimpl;

import com.ImperioElevator.ordermanagement.dao.OrderDao;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.valueobjects.CreateDateTime;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.UpdateDateTime;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

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

    @Override
    public Long insert(Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, created_date, updated_date, order_status) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime currentDateTime = LocalDateTime.now();

        CreateDateTime createdDate = order.createdDate() != null ? order.createdDate() : new CreateDateTime(currentDateTime);
        UpdateDateTime updatedDate = order.updatedDate() != null ? order.updatedDate() : new UpdateDateTime(currentDateTime);

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, order.userId().userId().id());
            ps.setTimestamp(2, Timestamp.valueOf(createdDate.createDateTime()));
            ps.setTimestamp(3, Timestamp.valueOf(updatedDate.updateDateTime()));
            ps.setString(4, order.orderStatus().name());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().longValue();
        } else {
            throw new SQLException("Creating client_order failed, no ID obtained.");
        }
    }


    @Override
    public Long update(Order order) throws SQLException {
        String sql = "UPDATE orders SET user_id = ?, updated_date = ?, order_status = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                order.userId().userId().id(),
                Timestamp.valueOf(order.updatedDate().updateDateTime()),
                order.orderStatus().toString(),
                order.orderId().id());
        return order.orderId().id();
    }

    @Override
    public Long updateStatus(Order order) throws SQLException {
        String sql = "UPDATE orders SET order_status = ?, updated_date = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                order.orderStatus().toString(),
                order.updatedDate() != null ? order.updatedDate().updateDateTime() : new java.sql.Timestamp(System.currentTimeMillis()),
                order.orderId().id());
        return order.orderId().id();
    }

    @Override
    public Long updateOrderEmailConfirmStatus(Long id) throws SQLException {
            String sql = "UPDATE orders SET order_status = 'CONFIRMED' WHERE id = ?";
            jdbcTemplate.update(sql, id);
        return id;
    }

    @Override
    //ToDo In doua selecturi odata scoate order si data despre order si in al doile order product
    // Get an object that hold all the data for the makeorder   2 request -1/ order info /2 order product slect the list where i will join with prodct
    public List<Object[]> getOrderWithExtraProducts(Long orderId) {
        //order information
        String sqlOrderInfo = "SELECT " +
                "    o.id, " +
                "    o.order_status, " +
                "    o.created_date, " +
                "    o.updated_date " +
                "FROM " +
                "    orders o " +
                "WHERE " +
                "    o.id = ?";

        //  order products with join on product table
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
            List<Object[]> orderInfo = jdbcTemplate.query(sqlOrderInfo, new Object[]{orderId}, (resultSet, i) -> new Object[]{
                    resultSet.getLong("id"),
                    resultSet.getString("order_status"),
                    resultSet.getTimestamp("created_date").toLocalDateTime(),
                    resultSet.getTimestamp("updated_date").toLocalDateTime()
            });

            List<Object[]> orderProducts = jdbcTemplate.query(sqlOrderProducts, new Object[]{orderId}, (resultSet, i) -> {
                Long parentProductId = resultSet.getLong("parent_product_id");
                return new Object[]{
                        resultSet.getLong("order_id"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("quantity"),
                        resultSet.getBigDecimal("price_product"),
                        resultSet.getLong("product_id"),
                        parentProductId != 0 ? parentProductId : null, // Replace 0 with null
                        resultSet.getString("image_path"),
                        resultSet.getString("category_type")
                };
            });

            List<Object[]> combinedResult = new ArrayList<>();
            combinedResult.addAll(orderInfo);
            combinedResult.addAll(orderProducts);

            return combinedResult;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }



    @Override
    public Long deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    @Override
    public Order findById(Long id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> mapResultSetToEntity(resultSet));
        } catch (EmptyResultDataAccessException e) {
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
                new User(new Id(userId), null, null),
                orderStatus,
                new CreateDateTime(createdDateTime),
                new UpdateDateTime(updatedDateTime),
              new ArrayList<>()
        );
    }

    @Override
    public Paginable<Order> findPaginableOrderByCreatedDate(LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM orders WHERE created_date BETWEEN ? AND ?";
        String sql = "SELECT * FROM orders WHERE created_date BETWEEN ? AND ? LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        Long offset = (page - 1) * numberOfOrders;

        Long totalItems = jdbcTemplate.queryForObject(countSql, new Object[]{Timestamp.valueOf(startDate), Timestamp.valueOf(endDate)}, Long.class);

        jdbcTemplate.query(sql, new Object[]{Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), numberOfOrders, offset}, resultSet -> {
            orders.add(mapResultSetToEntity(resultSet));
        });

        Long totalPages = (long) Math.ceil((double) totalItems / numberOfOrders);
        return new Paginable<>(orders, page, totalPages);
    }

    @Override
    public Paginable<Order> findPaginableOrderByUpdatedDate(LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM orders WHERE updated_date BETWEEN ? AND ?";
        String sql = "SELECT * FROM orders WHERE updated_date BETWEEN ? AND ? LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        Long offset = (page - 1) * numberOfOrders;

        Long totalItems = jdbcTemplate.queryForObject(countSql, new Object[]{Timestamp.valueOf(startDate), Timestamp.valueOf(endDate)}, Long.class);

        jdbcTemplate.query(sql, new Object[]{Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), numberOfOrders, offset}, resultSet -> {
            orders.add(mapResultSetToEntity(resultSet));
        });

        Long totalPages = (long) Math.ceil((double) totalItems / numberOfOrders);
        return new Paginable<>(orders, page, totalPages);
    }

    @Override
    public Paginable<Order> findPaginableOrderByCreatedDateAndStatus(LocalDateTime startDate, LocalDateTime endDate, Status status, Long numberOfOrders, Long page) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM orders WHERE created_date BETWEEN ? AND ? AND order_status = ?";
        String sql = "SELECT * FROM orders WHERE created_date BETWEEN ? AND ? AND order_status = ? LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        Long offset = (page - 1) * numberOfOrders;

        Long totalItems = jdbcTemplate.queryForObject(countSql, new Object[]{Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), status.name()}, Long.class);

        jdbcTemplate.query(sql, new Object[]{Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), status.name(), numberOfOrders, offset}, resultSet -> {
            orders.add(mapResultSetToEntity(resultSet));
        });

        Long totalPages = (long) Math.ceil((double) totalItems / numberOfOrders);
        return new Paginable<>(orders, page, totalPages);
    }

    @Override
    public List<Order> findLastCreatedOrders(Number limit) throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY id ASC LIMIT ?";
        List<Order> orders = new ArrayList<>();
        jdbcTemplate.query(sql, new Object[]{limit}, resultSet -> {
            orders.add(mapResultSetToEntity(resultSet));
        });
        return orders;
    }
}
