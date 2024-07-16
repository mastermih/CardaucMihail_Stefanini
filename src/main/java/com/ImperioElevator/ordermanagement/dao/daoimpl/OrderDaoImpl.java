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
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
public class OrderDaoImpl extends AbstractDao<Order> implements OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDaoImpl(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    private Timestamp setTimeToZero(LocalDateTime dateTime) {
        LocalDateTime dateOnly = dateTime.toLocalDate().atStartOfDay();
        return Timestamp.valueOf(dateOnly);
    }

    public Long insert(Order order) throws SQLException {
        String sql = "INSERT INTO `order` (user_id, created_date, updated_date, order_status) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, order.getUserId().getUserId().getId());
            ps.setTimestamp(2, setTimeToZero(order.getCreatedDate().getCreateDateTime()));
            ps.setTimestamp(3, setTimeToZero(order.getUpdatedDate().getUpdateDateTime()));
            ps.setString(4, order.getOrderStatus().toString());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().longValue();
        } else {
            throw new SQLException("Creating order failed, no ID obtained.");
        }
    }

    @Override
    public Long update(Order order) throws SQLException {
        String sql = "UPDATE `order` SET user_id = ?, updated_date = ?, order_status = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                order.getUserId().getUserId().getId(),
                order.getUpdatedDate().getUpdateDateTime(),
                order.getOrderStatus().toString(),
                order.getOrderId().getId());
        return order.getOrderId().getId();
    }


    @Override
    public Long deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM `order` WHERE id = ?";
        jdbcTemplate.update(sql, id);

        return id;
    }

    @Override
    public Order findById(Long id) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE id = ?";
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
        LocalDateTime createdDateTime = resultSet.getObject("created_date", LocalDateTime.class);
        LocalDateTime updatedDateTime = resultSet.getObject("updated_date", LocalDateTime.class);
        Status orderStatus = Status.valueOf(resultSet.getString("order_status"));
        return new Order(
                new Id(orderId),
                new User(new Id(userId), null, null),
                orderStatus,
                new CreateDateTime(createdDateTime),
                new UpdateDateTime(updatedDateTime)
        );
    }

    @Override
    public Paginable<Order> findPaginableOrderByCreatedDate(LocalDateTime startDate, LocalDateTime endDate,  Long numberOfOrders, Long page) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM `order` WHERE created_date BETWEEN ? AND ?";
        String sql = "SELECT * FROM `order` WHERE created_date BETWEEN ? AND ? LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        Long offset = (page - 1) * numberOfOrders;

        Long totalItems = jdbcTemplate.queryForObject(countSql, new Object[]{startDate, endDate}, Long.class);

        jdbcTemplate.query(sql, new Object[]{startDate, endDate, numberOfOrders, offset}, (resultSet) -> {
            orders.add(mapResultSetToEntity(resultSet));
        });

        Long totalPages = (long) Math.ceil((double) totalItems / numberOfOrders);
        return new Paginable<>(orders, page, totalPages);
    }

    @Override
    public Paginable<Order> findPaginableOrderByUpdatedDate(LocalDateTime startDate, LocalDateTime endDate, Long numberOfOrders, Long page) throws SQLException {

        String countSql = "SELECT COUNT(*) FROM `order` WHERE updated_date BETWEEN ? AND ?";
        String sql = "SELECT * FROM `order` WHERE updated_date BETWEEN ? AND ? LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        Long offset = (page - 1) * numberOfOrders;

        Long totalItems = jdbcTemplate.queryForObject(countSql, new Object[]{startDate, endDate}, Long.class);

        jdbcTemplate.query(sql, new Object[]{startDate, endDate, numberOfOrders, offset}, (resultSet) -> {
            orders.add(mapResultSetToEntity(resultSet));
        });

        Long totalPages = (long) Math.ceil((double) totalItems / numberOfOrders);
        return new Paginable<>(orders, page, totalPages);
    }

    // Maper rowMapper // 1 maper
    @Override
    public Paginable<Order> findPaginableOrderByCreatedDateAndStatus(LocalDateTime startDate, LocalDateTime endDate, Status status, Long numberOfOrders, Long page) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM `order` WHERE created_date BETWEEN ? AND ? AND order_status = ?";
        String sql = "SELECT * FROM `order` WHERE created_date BETWEEN ? AND ? AND order_status = ? LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        Long offset = (page - 1) * numberOfOrders;

        Long totalItems = jdbcTemplate.queryForObject(countSql, new Object[]{startDate, endDate, status.name()}, Long.class);

        jdbcTemplate.query(sql, new Object[]{startDate, endDate, status.name(), numberOfOrders, offset}, (resultSet) -> {
            orders.add(mapResultSetToEntity(resultSet));
        });

        Long totalPages = (long) Math.ceil((double) totalItems / numberOfOrders);
        return new Paginable<>(orders, page, totalPages);
    }

    //treb de testat
    @Override
    public List<Order> findLastCreatedOrders(Number limit) throws SQLException {
        String sql = "SELECT * FROM `order` ORDER BY id ASC LIMIT ?";
        List<Order> orders = new ArrayList<>();
        jdbcTemplate.query(sql, new Object[]{limit}, (resultSet) -> {
            orders.add(mapResultSetToEntity(resultSet));
        });
        return orders;
    }

}