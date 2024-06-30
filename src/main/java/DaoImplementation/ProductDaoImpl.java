package DaoImplementation;

import DaoWork.AbstractDao;
import Entity.Product;
import ValueObjects.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl extends AbstractDao<Product>
{

    public ProductDaoImpl(Connection connection)
    {
        super(connection);
    }

    @Override
    public void insert(Product product) throws SQLException {
        String sql = "INSERT IGNORE INTO product (product_id, category_id, product_brand, product_name, electricity_consumption, description, width, height, depth, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, product.getProduct_id().getId());
            statement.setLong(2, product.getCategory().getId());
            statement.setString(3, product.getProductBrand().getProductBrand());
            statement.setString(4, product.getProductName().getProductName());
            statement.setInt(5, product.getElectricityConsumption().getkWh());
            statement.setString(6, product.getDescription().getDescription());
            statement.setDouble(7, product.getWidth().getWidth());
            statement.setDouble(8, product.getHeight().getHeight());
            statement.setDouble(9, product.getDepth().getDepth());
            statement.setInt(10, product.getPrice().getProductPrice());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Product product) throws SQLException {
        String sql = "UPDATE product SET category_id = ?, product_brand = ?, product_name = ?, electricity_consumption = ?, description = ?, width = ?, height = ?, depth = ?, price = ? WHERE product_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, product.getCategory().getId());
            statement.setString(2, product.getProductBrand().getProductBrand());
            statement.setString(3, product.getProductName().getProductName());
            statement.setInt(4, product.getElectricityConsumption().getkWh());
            statement.setString(5, product.getDescription().getDescription());
            statement.setDouble(6, product.getWidth().getWidth());
            statement.setDouble(6, product.getWidth().getWidth());
            statement.setDouble(7, product.getHeight().getHeight());
            statement.setDouble(8, product.getDepth().getDepth());
            statement.setInt(9, product.getPrice().getProductPrice());
            statement.setLong(10, product.getProduct_id().getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteById(long id) throws SQLException {
        String sql = "DELETE FROM product WHERE product_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public Product findById(long id) throws SQLException {
        String sql = "SELECT * FROM product WHERE product_id = ?";
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

    public List<Product> getProductPagination(int numberOfProducts) throws SQLException {
        String sql = "SELECT * FROM product LIMIT ?";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, numberOfProducts);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return products;
    }

    public Product mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        ID productId = new ID(resultSet.getInt("product_id"));
        Category category = new Category(resultSet.getLong("category_id"), null, null); // Only the ID is relevant
        ProductName productName = new ProductName(resultSet.getString("product_name"));
        ProductBrand productBrand = new ProductBrand(resultSet.getString("product_brand"));
        ElectricityConsumption electricityConsumption = new ElectricityConsumption(resultSet.getInt("electricity_consumption"));
        Description description = new Description(resultSet.getString("description"));
        Width width = new Width(resultSet.getDouble("width"));
        Height height = new Height(resultSet.getDouble("height"));
        Depth depth = new Depth(resultSet.getDouble("depth"));
        Price price = new Price(resultSet.getInt("price"));

        return new Product(productId, price, width, height, depth, category, productBrand, productName, electricityConsumption, description);
    }
}