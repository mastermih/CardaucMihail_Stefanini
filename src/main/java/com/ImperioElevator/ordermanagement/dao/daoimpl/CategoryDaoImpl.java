package com.ImperioElevator.ordermanagement.dao.daoimpl;



import com.ImperioElevator.ordermanagement.entity.Category;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.*;
@Component
public class CategoryDaoImpl extends AbstractDao<Category> {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CategoryDaoImpl.class);

    public CategoryDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(Category category) throws SQLException {
        String sql = "INSERT INTO category (name, parent_id) VALUES (?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            logger.debug("Executing SQL for category insert: {}", sql);

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, category.name().name());
                if (category.parentId() != null) {
                    ps.setLong(2, category.parentId().id().id());
                } else {
                    ps.setNull(2, java.sql.Types.BIGINT);
                }
                return ps;
            }, keyHolder);

            logger.info("Successfully inserted Category with name: {}", category.name().name());

            if (keyHolder.getKey() != null) {
                return keyHolder.getKey().longValue();
            } else {
                throw new SQLException("Creating category failed, no ID obtained.");
            }

        } catch (SQLException ex) {
            logger.error("Failed to insert Category with name: {}", category.name().name(), ex);
            throw ex;
        }
    }

    @Override
    public Long update(Category category) throws SQLException {
        String sql = "UPDATE category SET name = ?, parent_id = ? WHERE id = ?";

        try {
            logger.debug("Executing SQL for category update: {}", sql);

            jdbcTemplate.update(sql,
                    category.name().name(),
                    category.parentId() != null ? category.parentId().id().id() : null,
                    category.id().id());

            logger.info("Successfully updated Category with id: {}", category.id().id());

            return category.id().id();

        } catch (DataAccessException ex) {
            logger.error("Failed to update Category with id: {}", category.id().id(), ex);
            throw ex;
        }
    }

    @Override
    public Long deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM category WHERE id = ?";

        try {
            logger.debug("Executing SQL to delete Category: {}", sql);

            jdbcTemplate.update(sql, id);

            logger.info("Successfully deleted Category with id: {}", id);

            return id;

        } catch (DataAccessException ex) {
            logger.error("Failed to delete Category with id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    public Category findById(Long id) throws SQLException {
        String sql = "SELECT * FROM category WHERE id = ?";

        try {
            logger.debug("Executing SQL to find Category by id: {}", sql);

            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> mapResultSetToEntity(resultSet));

        } catch (EmptyResultDataAccessException e) {
            logger.error("No Category found with id: {}", id, e);
            return null;
        } catch (DataAccessException ex) {
            logger.error("Failed to find Category with id: {}", id, ex);
            throw ex;
        }
    }

    @Override
    public Category mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Id id = new Id(resultSet.getLong("id"));
        Name name = new Name(resultSet.getString("name"));
        long parentIdValue = resultSet.getLong("parent_id");
        Category parent = null;

        if (!resultSet.wasNull()) {
            parent = new Category(new Id(parentIdValue), null, null);
        }

        return new Category(id, name, parent);
    }
}