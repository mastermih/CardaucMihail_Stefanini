package com.ImperioElevator.ordermanagement.dao.daoimpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;  // Make sure this import is included
import java.util.List;

import com.ImperioElevator.ordermanagement.dao.UserDao;
import com.ImperioElevator.ordermanagement.entity.TokenGenerator;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Role;
import com.ImperioElevator.ordermanagement.valueobjects.Email;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Name;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDaoImpl extends AbstractDao<User> implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    public Long insert(User user) throws SQLException {
        String sql = "INSERT INTO user (username, email, password, account_not_locked, phone_number) VALUES (?, ?, ?, ?, ?)";

        String tokenSql = "INSERT INTO token (order_id, user_id, token_type, token_value, is_enabled) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String token = TokenGenerator.generateToken();

        try {
            logger.debug("Executing creation of the user: {}", sql);

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.name().name());
                ps.setString(2, user.email().email());
                ps.setString(3, user.password());
                ps.setBoolean(4, user.accountNonLocked());
                ps.setString(5, user.phoneNumber());
                return ps;
            }, keyHolder);

            Long userId = keyHolder.getKey().longValue();
            logger.info("Successfully inserted User with userId: {}", userId);

            // Insert the token into the token table
            logger.debug("Inserting token for userId: {}", userId);
            jdbcTemplate.update(tokenSql, new Object[]{
                    null,
                    userId,
                    "USER",
                    token,
                    true
            });

            return userId;

        } catch (DataAccessException e) {
            logger.error("Error while creating the user: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Long update(User user) throws SQLException {
        String sql = "UPDATE user SET username = ?, email = ?, image = ?, phone_number = ? WHERE id = ? ";

        try {
            logger.debug("Execute the user update: {}", sql);
            jdbcTemplate.update(sql,
                    user.name().name(),
                    user.email().email(),
                    //   user.password(),             // password have to be removed
                    user.image(),
                    user.phoneNumber(),
                    user.userId().id());
            logger.info("Successfully updated Product with id: {}", user.userId().id());
            return user.userId().id();
        } catch (DataAccessException e) {
            logger.error("Failed to update the user: {}", user.userId().id(), e);
            throw e;
        }
    }

    @Override
    public Long deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM user WHERE id = ?";
        try {
            logger.debug("Executing SQL to delete User: {}", sql);

            jdbcTemplate.update(sql, id);
            logger.info("Successfully deleted user with id: {}", id);
            return id;
        } catch (DataAccessException e) {
            logger.error("Failed to delete the user with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public User findById(Long id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        try {
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
        String password = resultSet.getString("password");
        String phoneNumber = resultSet.getString("phone_number");
        String image = resultSet.getString("image");
        boolean account_not_locked = resultSet.getBoolean("account_not_locked");

        String roleSql = "SELECT r.role_name FROM roles r JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?";
        List<Role> roles = jdbcTemplate.query(roleSql, new Object[]{userId.id()}, (rs, rowNum) -> Role.valueOf(rs.getString("role_name")));

        // Return User object with multiple roles
        return new User(userId, userName, email, password, phoneNumber, image, roles, account_not_locked);
    }

    @Transactional
    public String confirmUserByEmailConfirmationLocked(String token) throws SQLException {
        // Update user account to unlocked using the token
        String updateUserSql = "UPDATE user SET account_not_locked = TRUE WHERE id = (SELECT user_id FROM token WHERE token_value = ? AND is_enabled = true AND token_type = 'USER')";

        try {
            logger.debug("Executing SQL to confirm User account by email: {}", token);

            // Update the user's account status using the token
            int rowsUpdated = jdbcTemplate.update(updateUserSql, token);
            if (rowsUpdated == 0) {
                // 0 friendly response nici o mila
                throw new SQLException("No user found with the provided confirmation token or more probabily you already confirmed the user :).");
            }

            logger.debug("User account unlocked using token: {}", token);


            logger.debug("Token disabled for token_value: {}", token);

            return token;

        } catch (DataAccessException ex) {
            logger.error("Failed to confirm User account by email for token: {}", token, ex);
            throw ex;
        }
    }


    @Override
    public void disableTokenAfterUserConfirmation(String token) throws SQLException {
        String sql = "UPDATE token SET is_enabled = false WHERE token_value = ?";
        try {
            logger.debug("Disabling token after user confirmation: {}", sql);
            jdbcTemplate.update(sql, token);
            logger.debug("Token has been disabled after user confirmation: {}", token);
        } catch (DataAccessException ex) {
            logger.error("Failed to disable token after user confirmation for token: {}", token, ex);
            throw ex;
        }
    }


    //Send roles in user_roles table
    @Override
    public void giveToUserRoles(Long userId, List<Long> roleIds) throws SQLException {
        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        try {
            for (Long roleId : roleIds) {
                jdbcTemplate.update(sql, userId, roleId);
                logger.info("Successfully assigned role_id: {} to userId: {}", roleId, userId);
            }
        } catch (DataAccessException e) {
            logger.error("Failed to assign roles for userId: {}", userId, e);
            throw e;
        }
    }


    @Override
    public Long getRoleIdFromRoleName(String roleName) throws SQLException {
        String sql = "SELECT id FROM roles WHERE role_name = ?";

        try {
            // query to fetch the role_id based on the role_name
            return jdbcTemplate.queryForObject(sql, new Object[]{roleName}, Long.class);
        } catch (DataAccessException e) {
            logger.error("Failed to fetch role_id for role: {}", roleName, e);
            throw new SQLException("Role not found for role_name: " + roleName);
        }
    }

    @Override
    public String getTheConfirmationToken(Long userId) throws SQLException {
        String sql = "SELECT token_value FROM token WHERE user_id = ? AND token_type = 'USER' AND is_enabled = TRUE";
        try {
            logger.debug("Selecting the user token with query: " + sql);
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, String.class);
        } catch (DataAccessException e) {
            logger.error("Failed to fetch confirmation token for user ID: {}", userId, e);
            throw new SQLException("Token not found for user ID: " + userId);
        }
    }

    @Override
    public Long addImageForUSer(Long userId, String imagePath) throws SQLException {
        String sql = "UPDATE user SET image = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql, imagePath, userId);
            logger.debug("Successfully added image path to user with id: " + userId);
        } catch (DataAccessException e) {
            logger.error("Failed to add image path for user with id: " + userId, e);
            throw new SQLException("Failed to add image path for user", e);
        }
        return userId;
    }

    @Override
    public String getUserImage(Long userId) throws SQLException {
        String sql = "SELECT image FROM user WHERE id = ?";
        try {
            logger.debug("Geting the user image " + sql);
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, String.class);
        } catch (DataAccessException e) {
            logger.error("Failed to get the user image ", e, sql);
            throw e;
        }

    }

    @Override
    public User findByUserEmail(String email) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        try {
            logger.debug("Executing SQL to find User by email: {}", sql);
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, (resultSet, i) -> mapResultSetToEntity(resultSet));
        } catch (EmptyResultDataAccessException e) {
            logger.error("No User found with email: {}", email, e);
            return null;
        } catch (DataAccessException ex) {
            logger.error("Failed to find User with email: {}", email, ex);
            throw ex;
        }
    }

    @Override
    public Long findUserIdByEmail(String email) throws SQLException {
        String sql = "SELECT id FROM user WHERE email = ?";
        try{
            logger.debug("Executing SQL to select UserID by email: {}", sql);
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, Long.class);
        }catch (EmptyResultDataAccessException e){
            logger.error("Failed to find UserId with email: {}", email, e);
            throw e;
        }
    }
}