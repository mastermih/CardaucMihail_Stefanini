package com.ImperioElevator.ordermanagement.dao.daoimpl;

import com.ImperioElevator.ordermanagement.dao.Dao;
import com.ImperioElevator.ordermanagement.dao.UserNotificationDao;
import com.ImperioElevator.ordermanagement.entity.UserNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

public class UserNotificationDaoImpl extends AbstractDao<UserNotification> implements UserNotificationDao {
    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    public UserNotificationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(UserNotification entity) throws SQLException {
        String sql = "INSERT INTO user_notification (user_id, notification_id, is_read) VALUES(?,?,?)";
        try{
            logger.debug("Inserting in user_notification values related to the notification " + sql);
            jdbcTemplate.update(sql, entity);
            return  entity.getNotificationId();
        }catch (DataAccessException ex){
            logger.error("Failed to insert in user_notification values related to the notification " + ex);
            throw ex;
        }
        }
}
