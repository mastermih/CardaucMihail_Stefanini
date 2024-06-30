package DaoImplementation;

import DaoWork.ProductDaoInterface;
import Entity.Product;
import JDBC.DatabaseConnectionDemo;
import ValueObjects.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProductDaoImpl implements ProductDaoInterface {

    private static final String INSERT_PRODUCT = "INSERT INTO product (product_id, type, product_brand, product_name, electricity_consumption, description, width, height, depth, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_PRODUCT = "UPDATE product SET type = ?, product_brand = ?, product_name = ?, electricity_consumption = ?, description = ?, width = ?, height = ?, depth = ?, price = ? WHERE product_id = ?";
    private static final String DELETE_PRODUCT = "DELETE FROM product WHERE product_id = ?";
    private static final String GET_PRODUCT_BY_ID = "SELECT * FROM product WHERE product_id = ?";
    private static final String GET_PRODUCTS_LIMITED = "SELECT * FROM product LIMIT ?";


    private static ProductDaoImpl instance;

    private ProductDaoImpl() {
        // Private constructor to prevent instantiation
    }
// Revizuim
    public static synchronized ProductDaoImpl getInstance() {
        if (instance == null) {
            instance = new ProductDaoImpl();
        }
        return instance;
    }

    @Override
    public void insert(Product product) throws SQLException {
        try (Connection connection = DatabaseConnectionDemo.open();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT)) {

            preparedStatement.setInt(1, product.getProduct_id().getId());
           // preparedStatement.setString(2, product.getType().name());
            preparedStatement.setString(3, product.getProductBrand().getProductBrand());
            preparedStatement.setString(4, product.getProductName().getProductName());
            preparedStatement.setInt(5, product.getElectricityConsumption().getkWh());
            preparedStatement.setString(6, product.getDescription().getDescription());
            preparedStatement.setDouble(7, product.getWidth().getWidth());
            preparedStatement.setDouble(8, product.getHeight().getHeight());
            preparedStatement.setDouble(9, product.getDepth().getDepth());
            preparedStatement.setInt(10, product.getPrice().getProductPrice());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(Product product) throws SQLException {
        try (Connection connection = DatabaseConnectionDemo.open();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT)) {

         //   preparedStatement.setString(1, product.getType().name());
            preparedStatement.setString(2, product.getProductBrand().getProductBrand());
            preparedStatement.setString(3, product.getProductName().getProductName());
            preparedStatement.setInt(4, product.getElectricityConsumption().getkWh());
            preparedStatement.setString(5, product.getDescription().getDescription());
            preparedStatement.setDouble(6, product.getWidth().getWidth());
            preparedStatement.setDouble(7, product.getHeight().getHeight());
            preparedStatement.setDouble(8, product.getDepth().getDepth());
            preparedStatement.setInt(9, product.getPrice().getProductPrice());
            preparedStatement.setInt(10, product.getProduct_id().getId());  // Set product_id as the last parameter


            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deleteById(int productId) throws SQLException {
        try (Connection connection = DatabaseConnectionDemo.open();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT)) {

            preparedStatement.setInt(1, productId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Product findById(int productId) throws SQLException {
        try (Connection connection = DatabaseConnectionDemo.open();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCT_BY_ID)) {

            preparedStatement.setInt(1, productId); // Set productId as parameter index 1
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
//                    return new Product(
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
                }
            }
        }
        return null;
    }

    @Override
    public List<Product> getProductPagination(int numberOfProducts) throws SQLException {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DatabaseConnectionDemo.open();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCTS_LIMITED)) {

            preparedStatement.setInt(1, numberOfProducts);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
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
////                    );
//                    products.add(product);
                }
            }
        }

        return products;
    }

}

