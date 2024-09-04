package com.ImperioElevator.ordermanagement.dao.daoimpl;

import java.awt.font.TextHitInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;  // Make sure this import is included

import com.ImperioElevator.ordermanagement.dao.UserDao;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Role;
import com.ImperioElevator.ordermanagement.valueobjects.Email;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Name;
import liquibase.sql.Sql;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.tags.EditorAwareTag;

@Repository
public class UserDaoImpl extends AbstractDao<User> implements UserDao {
private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    public Long insert(User user) throws SQLException {
        String sql = "INSERT INTO user (username, email, password, account_not_locked) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            logger.debug("Executing creation of the user: {}", sql);  // Log the SQL query

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.name().name());         // username
                ps.setString(2, user.email().email());       // email
                ps.setString(3, user.password());            // password
                ps.setBoolean(4, user.accountNonLocked());   // account locked status
                return ps;
            }, keyHolder);

            // Retrieve the auto-generated userId from the database
            if (keyHolder.getKey() != null) {
                Long generatedUserId = keyHolder.getKey().longValue();
                logger.info("Successfully inserted User with userId: {}", generatedUserId);
                return generatedUserId;
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }

        } catch (SQLException e) {
            logger.error("Error while creating the user: {}", e.getMessage());
            throw e;
        }
    }


    @Override
    public Long update(User user) throws SQLException{
        String sql =  "UPDATE user SET username = ?, email = ?, password = ?, role = ? ,account_not_locked = ? WHERE id = ? ";

        try{
            logger.debug("Execute the user update: {}", sql);
            jdbcTemplate.update(sql,
                    user.userId().id(),
                    user.name().name(),
                    user.email().email(),
                    user.password(),
                    user.role(),
                    user.accountNonLocked());
            logger.info("Successfully updated Product with id: {}", user.userId().id());
            return user.userId().id();
        }catch (DataAccessException e ){
            logger.error("Failed to update the user: {}", user.userId().id() ,e);
            throw e;
        }
    }
    @Override
    public Long deleteById(Long id) throws SQLException{
        String sql = "DELETE FROM user WHERE id = ?";
        try{
            logger.debug("Executing SQL to delete User: {}", sql);

            jdbcTemplate.update(sql, id);
            logger.info("Successfully deleted user with id: {}", id);
            return id;
        }catch (DataAccessException e){
            logger.error("Failed to delete the user with id: {}", id, e);
            throw e;
        }
    }
    @Override
    public User findById (Long id) throws SQLException{
      String sql  = "SELECT * FROM user WHERE id = ?";
      try{
          logger.debug("Executing SQL to find User by id: {}", sql);
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
    public User mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Id userId = new Id(resultSet.getLong("id"));
        Name userName = new Name(resultSet.getString("username"));
        Email email = new Email(resultSet.getString("email"));
        String password = new String(resultSet.getString("password"));
        Role role = Role.valueOf(new String(resultSet.getString("role")));
        boolean account_not_locked = resultSet.getBoolean("account_not_locked");

        return new User(userId, userName, email,password, role, account_not_locked);
    }

    @Override
    public Long confirmUserByEmailConfirmationLocked(Long id) throws SQLException {
        String sql = "UPDATE user SET account_not_locked = TRUE WHERE id = ?";
        try{
            logger.debug("Executing SQL to confirm new created user by email: {}", sql);
            jdbcTemplate.update(sql, id);
            return id;
        }catch (DataAccessException e){
            logger.error("Failed to confirm User by email with id: {}", id, e);
            throw e;
        }

    }

    @Override
    public Long giveToUserARole(Long userId, Role role) throws SQLException {
        String sql = "INSERT INTO user_roles (user_id, role_name) VALUES (? ,?)";

        try {
            logger.debug("Executing SQL for user_role insert: {}", sql);

            // Update the query to pass the userId (primitive) and role name (as String)
            jdbcTemplate.update(sql, new Object[] {
                    userId,                 // Primitive userId (Long)
                    role.name()             // Convert Role enum to its String representation
            });

            logger.info("Successfully inserted Role for userId: {}", userId);
            return userId;  // Return the userId after insertion (can be modified based on logic)

        } catch (DataAccessException e) {
            logger.error("Failed to insert role for userId: {}", userId, e);
            throw e;  // Rethrow the exception for further handling
        }
    }
    }
