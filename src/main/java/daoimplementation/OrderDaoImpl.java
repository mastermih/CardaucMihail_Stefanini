package daoimplementation;

import daowork.AbstractDao;
import entity.Order;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import valueobjects.CreateDateTime;
import valueobjects.Id;
import valueobjects.UpdateDateTime;

import java.sql.*;
import java.time.LocalDateTime;

//ToDo need to have separated dao class for order product realtionsh
public class OrderDaoImpl extends AbstractDao<Order>
{
    public OrderDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public long insert(Order order) throws SQLException {
        // need to fiend a solution how to take order id of new record (auto genrated java, auto incremented db, tablea separate squnace)
        String sql = "INSERT INTO `order` (user_id, created_date, updated_date) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            //statement.setLong(1, order.getOrderId().getId());
            statement.setLong(1, order.getUserId().getUserId().getId());
            statement.setObject(2, order.getCreatedDate().getCreateDateTime());
            statement.setObject(3, order.getUpdatedDate().getUpdateDateTime());
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
        String sql = "UPDATE `order` SET user_id = ?, created_date =?, updated_date = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, order.getUserId().getUserId().getId());
            statement.setObject(2, order.getCreatedDate().getCreateDateTime());
            statement.setObject(3, order.getUpdatedDate().getUpdateDateTime());
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
        return new Order(
                orderId,
                new User(userId, null, null),  // Initialize User with userId wrapped in Id
                new CreateDateTime(createdDateTime),
                new UpdateDateTime(updatedDateTime)
        );
    }

// Pad vaprosam
    public String getOrderWithProduct(long id) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                {

                }
            }
        }
        return null;
    }
}