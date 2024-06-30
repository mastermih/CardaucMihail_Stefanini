package DaoImplementation;

import DaoWork.OrderDaoInterface;
import Entity.Order;
import Entity.Product;
import JDBC.DatabaseConnectionDemo;
import ValueObjects.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//refactor le schimba pozitia (sub nas)
public class OrderDaoImpl implements OrderDaoInterface {
    private static final String INSERT_ORDER = "INSERT INTO `order` (id, order_id, user_id, product_id, price_product) VALUES (?, ?, ?, ?, ?);";
    private static final String CHECK_USER_EXISTS = "SELECT 1 FROM user WHERE user_id = ?;";
    private static final String CHECK_PRODUCT_EXISTS = "SELECT 1 FROM product WHERE product_id = ?;";
    private static final String GET_PRODUCT_PRICE = "SELECT price FROM product WHERE product_id = ?;";
    private static final String UPDATE_ORDER = "UPDATE `order` SET order_id = ?, user_id = ?, product_id = ?, price_product =? WHERE id = ?;";
    private static final String DELETE_ORDER = "DELETE FROM `order` WHERE id = ?;";
    private static final String GET_ORDER_BY_ID = "SELECT * FROM `order` WHERE id = ?;";
    private static final String JOIN_ORDER_PRODUCT = "SELECT o.id, o.order_id, o.user_id, o.product_id, o.price_product, p.product_id, p.type,p.category, p.product_brand, p.product_name, p.electricity_consumption, p.description, p.width, p.height, p.depth, p.price FROM `order` o INNER JOIN product p ON o.product_id = p.product_id WHERE o.id = ?;";
    private static OrderDaoImpl instance;

    private OrderDaoImpl() {
        // Private constructor to prevent instantiation
    }

    public static synchronized OrderDaoImpl getInstance() {
        if (instance == null) {
            instance = new OrderDaoImpl();
        }
        return instance;
    }

    @Override
    public void insert(Order order) throws SQLException {
        try (Connection connection = DatabaseConnectionDemo.open()) {
            // Check if user_id exists
            if (!userExists(connection, order.getUserId())) {
                throw new SQLException("User ID " + order.getUserId() + " does not exist.");
            }

            // Check if product_id exists
            if (!productExists(connection, order.getProduct().getProduct_id().getId())) {
                throw new SQLException("Product ID " +  order.getProduct().getProduct_id().getId() + " does not exist.");
            }

            // Retrieve the price from the product table and set it in the order
            int productPrice = getProductPrice(connection,  order.getProduct().getProduct_id().getId());
            order.setPrice(productPrice);

            // Insert order
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER)) {
                preparedStatement.setInt(1, order.getId());
                preparedStatement.setInt(2, order.getOrderId());
                preparedStatement.setInt(3, order.getUserId());
                preparedStatement.setInt(4, order.getProduct().getProduct_id().getId());
                preparedStatement.setInt(5, productPrice);

                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public void update(Order order) throws SQLException
    {
        try(Connection connection = DatabaseConnectionDemo.open();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER))
        {
            if(!userExists(connection, order.getUserId()))
            {
                throw new SQLException("User ID " + order.getUserId() + " does not exist.");
            }
            if (!productExists(connection, order.getProduct().getProduct_id().getId()))
            {
                throw new SQLException("Product ID " +  order.getProduct().getProduct_id().getId() + " does not exist.");
            }
            int producPrice = getProductPrice(connection,  order.getProduct().getProduct_id().getId());
            order.setPrice(producPrice);

            preparedStatement.setInt(1, order.getOrderId());
            preparedStatement.setInt(2, order.getUserId());
            preparedStatement.setInt(3,  order.getProduct().getProduct_id().getId());
            preparedStatement.setInt(4, order.getPrice());
            preparedStatement.setInt(5, order.getId());
            preparedStatement.executeUpdate();

        }
    }

    @Override
    public void deleteById(int id) throws SQLException
    {
        try(Connection connection = DatabaseConnectionDemo.open();
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ORDER))
        {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Order findById(int id) throws SQLException
    {
        try (Connection connection = DatabaseConnectionDemo.open();
        PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER_BY_ID))
        {
          preparedStatement.setInt(1, id);
          try (ResultSet resultSet = preparedStatement.executeQuery()){
              if(resultSet.next())
              {
//                  return new Order(
//                    resultSet.getInt("id"),
//                    resultSet.getInt("order_id"),
//                    resultSet.getInt("user_id"),
//                    resultSet.getInt("product_id"),
//                    resultSet.getInt("price_product")

                  //);


              }
          }
        }
        return null;
    }

    @Override
    public boolean userExists(Connection connection, int userId) throws SQLException {
        return exists(connection, CHECK_USER_EXISTS, userId);
    }

    @Override
    public boolean productExists(Connection connection, int productId) throws SQLException {
        return exists(connection, CHECK_PRODUCT_EXISTS, productId);
    }

    @Override
    public int getProductPrice(Connection connection, int productId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCT_PRICE)) {
            preparedStatement.setInt(1, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("price");
                } else {
                    throw new SQLException("Product ID " + productId + " does not exist.");
                }
            }
        }
    }
// returname Object nu String
    @Override
    public String getOrderWithProduct(int id) throws SQLException {
        StringBuilder result = new StringBuilder();
        try (Connection connection = DatabaseConnectionDemo.open();
             PreparedStatement preparedStatement = connection.prepareStatement(JOIN_ORDER_PRODUCT)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
//                    Order order = new Order(
//                            resultSet.getInt("id"),
//                            resultSet.getInt("order_id"),
//                            resultSet.getInt("user_id"),
//                          //  resultSet.getInt("product_id"),
//                            resultSet.getInt("price_product")
//                    );

//                    Product product = new Product(
//                            new ID(resultSet.getInt("product_id")),
//                            new Price(resultSet.getInt("price")),
//                            new Width(resultSet.getDouble("width")),
//                            new Height(resultSet.getDouble("height")),
//                            new Depth(resultSet.getDouble("depth")),
//                            Type.ElevatorType.valueOf(resultSet.getString("type")),
//                            new ProductBrand(resultSet.getString("product_brand")),
//                            new ProductName(resultSet.getString("product_name")),
//                            new ElectricityConsumption(resultSet.getInt("electricity_consumption")),
//                            new Description(resultSet.getString("description"))
//                    );
//
//                    result.append("Order Details: ")
//                           // .append(order.toString())
//                            .append("\nProduct Details: ")
//                            .append(product.toString());
                } else {
                    result.append("No order found with ID ").append(id);
                }
            }
        }
        return result.toString();
    }

    private boolean exists(Connection connection, String query, int id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
