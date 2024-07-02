package daoimplementation;

import daowork.AbstractDao;
import entity.Category;
import entity.Product;
import valueobjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public long insert(Product product) throws SQLException {
        String sql = "INSERT IGNORE INTO product (category_id, product_brand, product_name, electricity_consumption, product_description, product_width, product_height, product_depth, price) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
           // statement.setLong(1, product.getProductId().getId());
            statement.setLong(1, product.getCategory().getId().getId());
            statement.setString(2, product.getProductBrand().getProductBrand());
            statement.setString(3, product.getProductName().getProductName());
            statement.setInt(4, product.getElectricityConsumption().getkWh());
            statement.setString(5, product.getDescription().getDescription());
            statement.setDouble(6, product.getWidth().getWidth());
            statement.setDouble(7, product.getHeight().getHeight());
            statement.setDouble(8, product.getDepth().getDepth());
            statement.setInt(9, product.getPrice().getPrice());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating product failed ID problem");
                }
            }
        }
    }

    @Override
    public long update(Product product) throws SQLException {
        String sql = "UPDATE product SET category_id = ?, product_brand = ?, product_name = ?, electricity_consumption = ?, product_description = ?, product_width = ?, product_height = ?, product_depth = ?, price = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, product.getCategory().getId().getId());
            statement.setString(2, product.getProductBrand().getProductBrand());
            statement.setString(3, product.getProductName().getProductName());
            statement.setInt(4, product.getElectricityConsumption().getkWh());
            statement.setString(5, product.getDescription().getDescription());
            statement.setDouble(6, product.getWidth().getWidth());
            statement.setDouble(6, product.getWidth().getWidth());
            statement.setDouble(7, product.getHeight().getHeight());
            statement.setDouble(8, product.getDepth().getDepth());
            statement.setInt(9, product.getPrice().getPrice());
            statement.setLong(10, product.getProductId().getId());
            statement.executeUpdate();

        }
        return product.getProductId().getId();
    }

    @Override
    public long deleteById(long id) throws SQLException {
        String sql = "DELETE FROM product WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();

        }
        return  id;
    }

    @Override
    public Product findById(long id) throws SQLException {
        String sql = "SELECT * FROM product WHERE id = ?";
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
        Id productId = new Id(resultSet.getLong("id"));
        Id categoryId = new Id(resultSet.getLong("category_id"));
        Category category = new Category(categoryId, null, null); // Fetch category details if needed
        ProductBrand productBrand = new ProductBrand(resultSet.getString("product_brand"));
        ProductName productName = new ProductName(resultSet.getString("product_name"));
        ElectricityConsumption electricityConsumption = new ElectricityConsumption(resultSet.getInt("electricity_consumption"));
        Description description = new Description(resultSet.getString("product_description"));
        Width width = new Width(resultSet.getDouble("product_width"));
        Height height = new Height(resultSet.getDouble("product_height"));
        Depth depth = new Depth(resultSet.getDouble("product_depth"));
        Price price = new Price(resultSet.getInt("price"));

        return new Product(productId, price, width, height, depth, category, productBrand, productName, electricityConsumption, description);
    }
}