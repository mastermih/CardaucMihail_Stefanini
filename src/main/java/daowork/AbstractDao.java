package daowork;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import daointerface.Dao;

public abstract class AbstractDao<T> implements Dao<T>
{
    protected Connection connection;

    public AbstractDao(Connection connection)
    {
        this.connection = connection;
    }

    public abstract long  insert(T entity) throws SQLException;

    public abstract long  update(T entity) throws SQLException;

    public abstract long  deleteById(long id) throws SQLException;

    public abstract T findById(long id) throws SQLException;
    public abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;

}
