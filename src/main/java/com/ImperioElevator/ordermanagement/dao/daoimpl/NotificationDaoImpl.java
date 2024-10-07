package com.ImperioElevator.ordermanagement.dao.daoimpl;

import com.ImperioElevator.ordermanagement.dao.NotificationDao;
import com.ImperioElevator.ordermanagement.entity.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

//ToDo you know what
@Component
public class NotificationDaoImpl extends AbstractDao<Notification> implements NotificationDao {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger  = LoggerFactory.getLogger(NotificationDaoImpl.class);

    public NotificationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(Notification entity) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, message, is_read, created_date) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime currentDateTime = LocalDateTime.now();

        try{
            logger.debug("Inserting the notification in db " + entity);
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, entity.getUser());
                ps.setString(2, entity.getMessage());
                ps.setBoolean(3, entity.isRead());
                ps.setTimestamp(4, Timestamp.valueOf(currentDateTime));
                return ps;
            }, keyHolder);
    }catch (DataAccessException e){
            logger.error("Failed to insert the notification in th db " + e);
            throw e;
        }
        return entity.getNotificationId();
    }
}
