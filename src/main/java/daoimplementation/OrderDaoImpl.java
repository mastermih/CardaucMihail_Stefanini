package daoimplementation;

import daointerface.OrderDao;
import daowork.AbstractDao;
import entity.Order;
import entity.User;
import enumobects.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import valueobjects.CreateDateTime;
import valueobjects.Id;
import valueobjects.UpdateDateTime;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//ToDo need to have separated dao class for order product realtionship
public class OrderDaoImpl extends AbstractDao<Order> implements OrderDao
{
    public OrderDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public long insert(Order order) throws SQLException {
        String sql = "INSERT INTO `order` (user_id, created_date, updated_date, order_status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, order.getUserId().getUserId().getId());
            statement.setObject(2, order.getCreatedDate().getCreateDateTime());
            statement.setObject(3, order.getUpdatedDate().getUpdateDateTime());
            statement.setString(4, order.getOrderStatus().toString());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if(generatedKeys.next()){
                return generatedKeys.getLong(1);
            }else {
                throw new SQLException("Creating order filed, Id problem");
            }
            }
        }
    }

    @Override
    public long update(Order order) throws SQLException {
        String sql = "UPDATE `order` SET user_id = ? , updated_date = ?, order_status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, order.getUserId().getUserId().getId());
            statement.setObject(2, order.getUpdatedDate().getUpdateDateTime());
            statement.setString(3, order.getOrderStatus().toString());
            statement.setLong(4, order.getOrderId().getId());
            statement.executeUpdate();

        }
        return order.getOrderId().getId();
    }

    @Override
    public long deleteById(long id) throws SQLException {
        String sql = "DELETE FROM `order` WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
        return id;
    }

    @Override
    public Order findById(long id) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEntity(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public Order mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Id orderId = new Id(resultSet.getLong("id"));
        Id userId = new Id(resultSet.getLong("user_id"));
        LocalDateTime createdDateTime = resultSet.getObject("created_date", LocalDateTime.class);
        LocalDateTime updatedDateTime = resultSet.getObject("updated_date", LocalDateTime.class);
        Status orderStatus = Status.valueOf(resultSet.getString("order_status"));
        return new Order(
                orderId,
                new User(userId, null, null),  // Initialize User with userId wrapped in Id
                orderStatus,
                new CreateDateTime(createdDateTime),
                new UpdateDateTime(updatedDateTime)
        );
    }

    @Override
    public List<Order> findPaginableOrderByCreatedDate(LocalDateTime createdDate, int numberOfOrders) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE created_date = ? LIMIT ?";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, createdDate.toString());
            statement.setInt(2, numberOfOrders);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                  orders.add(mapResultSetToEntity(resultSet));
                }
            }
            return orders;
        }
    }

    @Override
    public List<Order> findPaginableOrderByUpdatedDate(LocalDateTime updatedDate, int numberOfOrders) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE updated_date = ? LIMIT ?";
        List<Order> orders = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, updatedDate.toString());
            statement.setInt(2, numberOfOrders);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    orders.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return orders;
    }

    @Override
    public List<Order> findPaginableOrderByCreatedDateAndStatus(LocalDateTime createdDate, Status status, int numberOfOrders) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE created_date = ? AND order_status = ? LIMIT ?";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, createdDate);
            statement.setString(2, status.name());
            statement.setInt(3, numberOfOrders);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return orders;
    }

}