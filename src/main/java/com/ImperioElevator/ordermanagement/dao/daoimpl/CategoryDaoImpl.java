package com.ImperioElevator.ordermanagement.dao.daoimpl;



import com.ImperioElevator.ordermanagement.entity.Category;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Name;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
@Repository
public class CategoryDaoImpl extends AbstractDao<Category> {
    private final JdbcTemplate jdbcTemplate;


    public CategoryDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Long insert(Category category) throws SQLException {
        String sql = "INSERT INTO category (name, parent_id) VALUES (?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getName().getName());
            if (category.getParentId() != null) {
                ps.setLong(2, category.getParentId().getId().getId());
            } else {
                ps.setNull(2, java.sql.Types.BIGINT);
            }
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().longValue();
        } else {
            throw new SQLException("Creating category failed, no ID obtained.");
        }
    }


    @Override
    public Long update(Category category) throws SQLException
    {
        String sql = "UPDATE category SET name = ?, parent_id = ? WHERE id = ?";
         jdbcTemplate.update(sql,
         category.getName().getName(),
         category.getParentId() != null ? category.getParentId().getId().getId() : null,  // Handle null parent ID
         category.getId().getId());
         return category.getId().getId();
    }

    @Override
    public Long deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM category WHERE id = ?";
         jdbcTemplate.update(sql, id);
         return id;
    }

    @Override
    public Category findById(Long id) throws SQLException {
        String sql = "SELECT * FROM category WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> mapResultSetToEntity(resultSet));
        }catch (EmptyResultDataAccessException e){
            return null;
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
