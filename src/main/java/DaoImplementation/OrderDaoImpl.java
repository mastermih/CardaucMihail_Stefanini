package DaoImplementation;

import DaoWork.AbstractDao;
import Entity.Order;

import java.sql.*;

public class OrderDaoImpl extends AbstractDao<Order> {

    public OrderDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Order order) throws SQLException {
        String sql = "INSERT INTO `order` (order_id, user_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, order.getOrderId());
            statement.setLong(2, order.getUserId());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Order order) throws SQLException {
        String sql = "UPDATE `order` SET user_id = ? WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, order.getUserId());
            statement.setLong(2, order.getOrderId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteById(long id) throws SQLException {
        String sql = "DELETE FROM `order` WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public Order findById(long id) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE order_id = ?";
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
        long orderId = resultSet.getLong("order_id");
        long userId = resultSet.getLong("user_id");

        return new Order(orderId, userId);
    }


    public String getOrderWithProduct(long id) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Implement logic to convert ResultSet to string representation of Order with Product
                }
            }
        }
        return null;
    }
}