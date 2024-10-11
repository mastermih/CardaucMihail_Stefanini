package com.ImperioElevator.ordermanagement.dao.daoimpl;

import com.ImperioElevator.ordermanagement.dao.UserNotificationDao;
import com.ImperioElevator.ordermanagement.entity.UserNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
@Repository
public class UserNotificationDaoImpl extends AbstractDao<UserNotification> implements UserNotificationDao {
    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    public UserNotificationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insertUserNotification(UserNotification entity) throws SQLException {
        String sql = "INSERT INTO user_notifications (user_id, notification_id, is_read, is_disabled) VALUES(?,?,?,?)";
        try{
            logger.debug("Inserting in user_notification values related to the notification " + sql);
            jdbcTemplate.update(sql,
                    entity.getUserId(),
                    entity.getNotificationId(),
                    entity.getRead(),
                    entity.getDisabled());
        return entity.getNotificationId();
        }catch (DataAccessException ex){
            logger.error("Failed to insert in user_notification values related to the notification " + ex);
            throw ex;
        }
        }

    @Override
    public Long notificationIsRead(Long userId) throws SQLException {
        String sql = "UPDATE user_notifications SET is_read = 1 WHERE user_id = ?";
        try {
             logger.debug("Set the notifications on read " + sql);
              jdbcTemplate.update(sql, userId);
              return userId;
        }catch (DataAccessException e){
            logger.error("Failed to add the is read to the user notifications " + e);
            throw e;
        }
    }

    @Override
    public Long notificationIsDisabled(Long notificationId, Long userId) throws SQLException {
        String sql = "UPDATE user_notifications SET is_disabled = 1 WHERE notification_id = ? AND user_id = ?";
        try{
            logger.debug("Set the notification is disabled " + sql);
            jdbcTemplate.update(sql, notificationId, userId);
            return notificationId;
        }catch (DataAccessException e){
            logger.error("Failed to disable the notification " + e);
            throw e;
        }
    }
}
