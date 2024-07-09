package com.ImperioElevator.ordermanagement.dao.daoimpl;



import com.ImperioElevator.ordermanagement.dao.Dao;
import com.ImperioElevator.ordermanagement.entity.Category;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.valueobjects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class ProductDaoImpl extends AbstractDao<Product> implements Dao<Product> {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public ProductDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(Product product) throws SQLException {
        String sql = "INSERT INTO product (category_id, product_brand, product_name, electricity_consumption, product_description, product_width, product_height, product_depth, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, product.getCategory().getId().getId());
            ps.setString(2, product.getProductBrand().getProductBrand());
            ps.setString(3, product.getProductName().getProductName());
            ps.setInt(4, product.getElectricityConsumption().getkWh());
            ps.setString(5, product.getDescription().getDescription());
            ps.setDouble(6, product.getWidth().getWidth());
            ps.setDouble(7, product.getHeight().getHeight());
            ps.setDouble(8, product.getDepth().getDepth());
            ps.setInt(9, product.getPrice().getPrice());
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
        String sql = "UPDATE product SET category_id = ?, product_brand = ?, product_name = ?, electricity_consumption = ?, product_description = ?, product_width = ?, product_height = ?, product_depth = ?, price = ? WHERE id = ?";

        jdbcTemplate.update(sql, product.getCategory().getId().getId(),
            product.getProductBrand().getProductBrand(),
            product.getProductName().getProductName(),
            product.getElectricityConsumption().getkWh(),
            product.getDescription().getDescription(),
            product.getWidth().getWidth(),
            product.getHeight().getHeight(),
            product.getDepth().getDepth(),
            product.getPrice().getPrice(),
            product.getProductId().getId());
        return product.getProductId().getId();
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
        }catch (EmptyResultDataAccessException e){
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
        Category category = new Category(categoryId, null, null);

        return new Product(productId, price, width, height, depth, category, productBrand, productName, electricityConsumption, description);
    }
}
