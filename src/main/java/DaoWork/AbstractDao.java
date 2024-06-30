package DaoWork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao<T>
{
    protected Connection connection;

    public AbstractDao(Connection connection)
    {
        this.connection = connection;
    }

    public abstract void insert(T entity) throws SQLException;

    public abstract void update(T entity) throws SQLException;

    public abstract void deleteById(long id) throws SQLException;

    public abstract T findById(long id) throws SQLException;
    public abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;
}
