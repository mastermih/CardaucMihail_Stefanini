package com.ImperioElevator.ordermanagement.dao.daoimpl;

import com.ImperioElevator.ordermanagement.dao.NotificationDao;
import com.ImperioElevator.ordermanagement.entity.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationDaoImpl extends AbstractDao<Notification> implements NotificationDao {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger  = LoggerFactory.getLogger(NotificationDaoImpl.class);

    public NotificationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(Notification entity) throws SQLException {
        String sql = "INSERT INTO notifications (message, created_date) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime currentDateTime = LocalDateTime.now();

        try {
            logger.debug("Inserting the notification in db " + entity);
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getMessage());
                ps.setTimestamp(2, Timestamp.valueOf(currentDateTime));
                return ps;
            }, keyHolder);

            // Retrieve the generated key for (notificationId)
            if (keyHolder.getKey() != null) {
                return keyHolder.getKey().longValue();
            } else {
                throw new SQLException("Failed to retrieve the generated notification ID.");
            }
        } catch (DataAccessException e) {
            logger.error("Failed to insert the notification in th db " + e);
            throw e;
        }
    }

//    //toDO it seams to be extra here
//    @Override
//    public Notification findById(Long id) throws SQLException {
//        String sql = "SELECT user_id, message, is_read FROM notifications WHERE user_id";
//        return super.findById(id);
//    }

    @Override
    public List<Notification> getNotifications(Long userId) {
        String sql = "SELECT n.id, n.message, n.created_date FROM notifications n "+
                "JOIN user_notifications un ON n.id = un.notification_id "+
                "WHERE un.user_id = ? AND un.is_disabled = 0";
        try{
            logger.debug("Get the notification of the customer create order from db " + sql);
            return jdbcTemplate.query(sql, new Object[]{userId}, (resultSet, i) -> mapResultSetToEntity(resultSet));
        }catch (DataAccessException e) {
            logger.error("Failed to get the customer create order notification from db " + e);
        throw e;
        }
    }

    @Override
    public Long insertNotificationWithAttachment(Notification entity, ByteArrayResource attachment) {
      // String sql = ""

        return 0L;
    }


    @Override
    public Notification mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Notification.NotificationBuilder()
                .notificationId(resultSet.getLong("id"))
                .message(resultSet.getString("message"))
                .createDate(resultSet.getTimestamp("created_date").toLocalDateTime())
                .build();
    }

}
