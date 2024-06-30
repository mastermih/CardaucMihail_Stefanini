package DaoWork;

import Entity.Order;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderDaoInterface extends DaoMain<Order> {

    // Check if a user exists
    boolean userExists(Connection connection, int userId) throws SQLException;

    // Check if a product exists
    boolean productExists(Connection connection, int productId) throws SQLException;

    // Get the price of a product by its ID
    int getProductPrice(Connection connection, int productId) throws SQLException;
    String getOrderWithProduct(int id) throws SQLException;
}
