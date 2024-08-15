package com.ImperioElevator.ordermanagement.dao.daoimpl;

import com.ImperioElevator.ordermanagement.dao.ProductDao;
import com.ImperioElevator.ordermanagement.entity.Category;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.enumobects.CategoryType;
import com.ImperioElevator.ordermanagement.valueobjects.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDaoImpl extends AbstractDao<Product> implements ProductDao {
    private final JdbcTemplate jdbcTemplate;

    public ProductDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(Product product) throws SQLException {
        String sql = "INSERT INTO product (category_id, product_brand, product_name, electricity_consumption, product_description, product_width, product_height, product_depth, price, image_path, category_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, product.category().id().id());
            ps.setString(2, product.productBrand().getProductBrand());
            ps.setString(3, product.productName().getProductName());
            ps.setDouble(4, product.electricityConsumption().getkWh());
            ps.setString(5, product.description().getDescription());
            ps.setDouble(6, product.width().getWidth());
            ps.setDouble(7, product.height().getHeight());
            ps.setDouble(8, product.depth().getDepth());
            ps.setDouble(9, product.price().price());
            ps.setString(10, product.path().getPath());
            ps.setString(11, product.categoryType().name());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().longValue();
        } else {
            throw new SQLException("Creating product failed, no ID obtained.");
        }
    }

    @Override
    public Long update(Product product) throws SQLException {
        String sql = "UPDATE product SET category_id = ?, product_brand = ?, product_name = ?, electricity_consumption = ?, product_description = ?, product_width = ?, product_height = ?, product_depth = ?, price = ?, category_type = ? WHERE id = ?";

        jdbcTemplate.update(sql, product.category().id().id(),
                product.productBrand().getProductBrand(),
                product.productName().getProductName(),
                product.electricityConsumption().getkWh(),
                product.description().getDescription(),
                product.width().getWidth(),
                product.height().getHeight(),
                product.depth().getDepth(),
                product.price().price(),
                product.productId().id(),
                product.categoryType().toString());

        return product.productId().id();
    }

    @Override
    public Long deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    @Override
    public Product findById(Long id) throws SQLException {
        String sql = "SELECT * FROM product WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> mapResultSetToEntity(resultSet));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Product mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Id productId = new Id(resultSet.getLong("id"));
        Id categoryId = new Id(resultSet.getLong("category_id"));
        ProductBrand productBrand = new ProductBrand(resultSet.getString("product_brand"));
        ProductName productName = new ProductName(resultSet.getString("product_name"));
        ElectricityConsumption electricityConsumption = new ElectricityConsumption(resultSet.getInt("electricity_consumption"));
        Description description = new Description(resultSet.getString("product_description"));
        Width width = new Width(resultSet.getDouble("product_width"));
        Height height = new Height(resultSet.getDouble("product_height"));
        Depth depth = new Depth(resultSet.getDouble("product_depth"));
        Price price = new Price(resultSet.getInt("price"));
        Image image = new Image(resultSet.getString("image_path"));
        CategoryType categoryType = CategoryType.valueOf(resultSet.getString("category_type"));
        Category category = new Category(categoryId, null, null);

        return new Product(productId, price, width, height, depth, category, productBrand, productName, electricityConsumption, description, image, categoryType);
    }

    @Override
    public List<Product> fiendProductForMainPage(Long limit, String categoryType) {
        String sql = "SELECT * FROM product WHERE category_type = ? ORDER BY category_type ASC LIMIT ?";
        List<Product> products = new ArrayList<>();
        jdbcTemplate.query(sql, new Object[]{categoryType, limit}, (result) -> {
            products.add(mapResultSetToEntity(result));
        });
        return products;
    }

    @Override
    public List<Product> fiendProductByName(String name) {
        String sql = "SELECT * FROM product WHERE product_name LIKE ? AND category_type NOT IN ('Elevator')";
        String searchQuery = "%" + name + "%";
        return jdbcTemplate.query(sql, new Object[]{searchQuery}, (result, i) ->
                mapResultSetToEntity(result));
    }

    @Override
    public List<Product> filterProducts(FilterComponents filterComponents) {
        StringBuilder sql = new StringBuilder("SELECT * FROM product WHERE 1=1");
        if (filterComponents.getCategoryType() != null) {
            sql.append(" AND category_type = ?");
        }
        if (filterComponents.getMinPrice() != null && filterComponents.getMaxPrice() != null) {
            sql.append(" AND price BETWEEN ? AND ?");
        }
        else if (filterComponents.getMinPrice() != null) {
            sql.append(" AND price >= ?");
        } else if (filterComponents.getMaxPrice() != null) {
            sql.append(" AND price <= ?");
        }
        if (filterComponents.getProductBrand() != null) {
            sql.append(" AND product_brand = ?");
        }
        if (filterComponents.getProductName() != null) {
            sql.append(" AND product_name = ?");
        }
        if (filterComponents.getElectricityConsumption() != null) {
            sql.append(" AND electricity_consumption = ?");
        }
        return jdbcTemplate.query(sql.toString(), preparedStatement -> {
            int index = 1;

            // Set parameters using PreparedStatement
            if (filterComponents.getCategoryType() != null) {
                preparedStatement.setString(index++, String.valueOf(filterComponents.getCategoryType()));
            }
            if (filterComponents.getMaxPrice() != null && filterComponents.getMinPrice() != null) {
                preparedStatement.setDouble(index++, filterComponents.getMinPrice());
                preparedStatement.setDouble(index++, filterComponents.getMaxPrice());
            } else if (filterComponents.getMinPrice() != null) {
                preparedStatement.setDouble(index++, filterComponents.getMinPrice());
            } else if (filterComponents.getMaxPrice() != null) {
                preparedStatement.setDouble(index++, filterComponents.getMaxPrice());
            }
            if (filterComponents.getProductBrand() != null) {
                preparedStatement.setString(index++, filterComponents.getProductBrand().getProductBrand());
            }
//            if (filterComponents.getProductName() != null) {
//                preparedStatement.setString(index++, "%" + filterComponents.getProductName().getProductName() + "%");

                if (filterComponents.getProductName() != null) {
                    preparedStatement.setString(index++,  filterComponents.getProductName().getProductName());
            }
            if (filterComponents.getElectricityConsumption() != null) {
                preparedStatement.setDouble(index++, filterComponents.getElectricityConsumption());
            }

        }, (resultSet, i) -> mapResultSetToEntity(resultSet));
    }
}