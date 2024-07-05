package daowork;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import daointerface.Dao;
import entity.OrderProduct;
import entity.Product;
import valueobjects.Id;

//Toate la un loc DAO
//Implementeaza totate metodele din Abstract
public abstract class AbstractDao<T> implements Dao<T> {
    protected Connection connection;

    public AbstractDao(Connection connection)
    {
        this.connection = connection;
    }

    public abstract Long insert(T entity) throws SQLException;

    public abstract Long update(T entity) throws SQLException;

    @Override
    public Long deleteById(Long id) throws SQLException {
        throw new UnsupportedOperationException("Not implemented");

    }

    public abstract T findById(Long id) throws SQLException;

    public abstract Long deleteById(Long orderId, Long productId) throws SQLException;

    public abstract OrderProduct findById(Long orderId, Long productId) throws SQLException;

    public abstract Id deleteById(Id id) throws SQLException;

    public abstract Product findById(Id id) throws SQLException;

    public abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;

}
