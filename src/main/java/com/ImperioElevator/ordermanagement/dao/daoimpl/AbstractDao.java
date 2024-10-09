package com.ImperioElevator.ordermanagement.dao.daoimpl;

import com.ImperioElevator.ordermanagement.dao.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao<T> implements Dao<T> {

    public Long insert(T entity) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }

    public Long update(T entity) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Long deleteById(Long id) throws SQLException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public T findById(Long id) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");

    }

    public  T mapResultSetToEntity(ResultSet resultSet) throws SQLException{
        throw new UnsupportedOperationException("Not implemented");

    }
}
