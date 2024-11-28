package com.ImperioElevator.ordermanagement.dao.daoimpl;

import com.ImperioElevator.ordermanagement.dao.ProductDao;
import com.ImperioElevator.ordermanagement.entity.Category;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.enumobects.CategoryType;
import com.ImperioElevator.ordermanagement.valueobjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    public ProductDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(Product product) throws SQLException {
        String sql = "INSERT INTO product (category_id, product_brand, product_name, electricity_consumption, product_description, product_width, product_height, product_depth, price, image_path, category_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            logger.debug("Executing SQL for product insert: {}", sql);

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, product.category().id().id());
                ps.setString(2, product.productBrand().getProductBrand());
                ps.setString(3, product.productName().productName());
                ps.setDouble(4, product.electricityConsumption().getkWh());
                ps.setString(5, product.description().getDescription());
                ps.setDouble(6, product.width().getWidth());
                ps.setDouble(7, product.height().getHeight());
                ps.setDouble(8, product.depth().getDepth());
                ps.setBigDecimal(9, product.price().price());
                ps.setString(10, product.path().getPath());
                ps.setString(11, String.valueOf(product.categoryType()));
                return ps;
            }, keyHolder);

            logger.info("Successfully inserted Product with name: {}", product.productName().productName());

            if (keyHolder.getKey() != null) {
                return keyHolder.getKey().longValue();
            } else {
                throw new SQLException("Creating product failed, no ID obtained.");
            }

        } catch (SQLException ex) {
            logger.error("Failed to insert Product with name: {}", product.productName().productName(), ex);
            throw ex;
        }
    }

    @Override
    public Long update(Product product) throws SQLException {
        String sql = "UPDATE product SET category_id = ?, product_brand = ?, product_name = ?, electricity_consumption = ?, product_description = ?, product_width = ?, product_height = ?, product_depth = ?, price = ?, category_type = ? WHERE id = ?";

        try {
            logger.debug("Executing SQL for product update: {}", sql);

            jdbcTemplate.update(sql,
                    product.category().id().id(),
                    product.productBrand().getProductBrand(),
                    product.productName().productName(),
                    product.electricityConsumption().getkWh(),
                    product.description().getDescription(),
                    product.width().getWidth(),
                    product.height().getHeight(),
                    product.depth().getDepth(),
                    product.price().price(),
                    String.valueOf(product.categoryType()),
                    product.productId().id());

            logger.info("Successfully updated Product with id: {}", product.productId().id());

            return product.productId().id();

        } catch (DataAccessException ex) {
            logger.error("Failed to update Product with id: {}", product.productId().id(), ex);
            throw ex;
        }
    }

    @Override
    public Long deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM product WHERE id = ?";

        try {
            logger.debug("Executing SQL to delete Product: {}", sql);

            jdbcTemplate.update(sql, id);

            logger.info("Successfully deleted Product with id: {}", id);

            return id;

        } catch (DataAccessException ex) {
            logger.error("Failed to delete Product with id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    public Product findById(Long id) throws SQLException {
        String sql = "SELECT * FROM product WHERE id = ?";

        try {
            logger.debug("Executing SQL to find Product by id: {}", sql);

            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> mapResultSetToEntity(resultSet));

        } catch (EmptyResultDataAccessException e) {
            logger.error("No Product found with id: {}", id, e);
            return null;
        } catch (DataAccessException ex) {
            logger.error("Failed to find Product with id: {}", id, ex);
            throw ex;
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
        Price price = new Price(resultSet.getBigDecimal("price"));
        Image image = new Image(resultSet.getString("image_path"));
        CategoryType categoryType = CategoryType.valueOf(resultSet.getString("category_type"));
        Category category = new Category(categoryId, null, null);

        return new Product(productId, price, width, height, depth, category, productBrand, productName, electricityConsumption, description, image, categoryType);
    }

    @Override
    public List<Product> fiendProductForMainPage(Long limit, String categoryType) {
        String sql = "SELECT * FROM product WHERE category_type = ? ORDER BY category_type ASC LIMIT ?";
        List<Product> products = new ArrayList<>();

        try {
            logger.debug("Executing SQL to find Products for main page: {}", sql);

            jdbcTemplate.query(sql, new Object[]{categoryType, limit}, (result) -> {
                products.add(mapResultSetToEntity(result));
            });

            logger.info("Successfully retrieved Products for main page with categoryType: {}", categoryType);

            return products;

        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve Products for main page with categoryType: {}", categoryType, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Product> fiendProductByName(String name) {
        String sql = "SELECT * FROM product WHERE product_name LIKE ? AND category_type NOT IN ('Elevator')";
        String searchQuery = "%" + name + "%";

        try {
            logger.debug("Executing SQL to find Products by name: {}", sql);

            return jdbcTemplate.query(sql, new Object[]{searchQuery}, (result, i) -> mapResultSetToEntity(result));

        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve Products by name: {}", name, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Paginable<Product> filterProducts(FilterComponents filterComponents, Long page, Long pageSize) {
        StringBuilder sql = new StringBuilder("SELECT * FROM product WHERE 1=1");
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM product WHERE 1=1");

        List<Object> params = new ArrayList<>();
        List<Object> countParams = new ArrayList<>();

        if (filterComponents.getCategoryType() != null) {
            sql.append(" AND category_type = ?");
            countSql.append(" AND category_type = ?");
            params.add(filterComponents.getCategoryType());
            countParams.add(filterComponents.getCategoryType());
        }
        if (filterComponents.getMinPrice() != null && filterComponents.getMaxPrice() != null) {
            sql.append(" AND price BETWEEN ? AND ?");
            countSql.append(" AND price BETWEEN ? AND ?");
            params.add(filterComponents.getMinPrice());
            params.add(filterComponents.getMaxPrice());
            countParams.add(filterComponents.getMinPrice());
            countParams.add(filterComponents.getMaxPrice());
        } else if (filterComponents.getMinPrice() != null) {
            sql.append(" AND price >= ?");
            countSql.append(" AND price >= ?");
            params.add(filterComponents.getMinPrice());
            countParams.add(filterComponents.getMinPrice());
        } else if (filterComponents.getMaxPrice() != null) {
            sql.append(" AND price <= ?");
            countSql.append(" AND price <= ?");
            params.add(filterComponents.getMaxPrice());
            countParams.add(filterComponents.getMaxPrice());
        }
        if (filterComponents.getProductBrand() != null) {
            sql.append(" AND product_brand = ?");
            countSql.append(" AND product_brand = ?");
            params.add(filterComponents.getProductBrand().getProductBrand());
            countParams.add(filterComponents.getProductBrand().getProductBrand());
        }
        if (filterComponents.getProductName() != null) {
            sql.append(" AND product_name = ?");
            countSql.append(" AND product_name = ?");
            params.add(filterComponents.getProductName().productName());
            countParams.add(filterComponents.getProductName().productName());
        }
        if (filterComponents.getElectricityConsumption() != null) {
            sql.append(" AND electricity_consumption = ?");
            countSql.append(" AND electricity_consumption = ?");
            params.add(filterComponents.getElectricityConsumption());
            countParams.add(filterComponents.getElectricityConsumption());
        }

        Long offset = (page - 1) * pageSize;
        sql.append(" ORDER BY id ASC LIMIT ? OFFSET ?");

        params.add(pageSize);
        params.add(offset);

        try {
            logger.debug("Executing SQL to filter Products: {}", sql);

            Long totalItems = jdbcTemplate.queryForObject(countSql.toString(), countParams.toArray(), Long.class);

            Long totalPages = (long) Math.ceil((double) totalItems / pageSize);

            List<Product> products = jdbcTemplate.query(sql.toString(), params.toArray(), (resultSet, i) -> mapResultSetToEntity(resultSet));

            logger.info("Successfully filtered Products with page: {} and pageSize: {}", page, pageSize);

            return new Paginable<>(products, page, totalPages);

        } catch (DataAccessException ex) {
            logger.error("Failed to filter Products", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Product findProductId(Long id) throws SQLException {
        String sql = "SELECT * FROM product WHERE id = ?";

        try {
            logger.debug("Executing SQL to find Product by id: {}", sql);

            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> mapResultSetToEntity(resultSet));

        } catch (EmptyResultDataAccessException e) {
            logger.error("No Product found with id: {}", id, e);
            return null;
        } catch (DataAccessException ex) {
            logger.error("Failed to find Product with id: {}", id, ex);
            throw ex;
        }
    }
}