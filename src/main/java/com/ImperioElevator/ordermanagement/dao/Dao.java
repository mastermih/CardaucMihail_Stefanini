package com.ImperioElevator.ordermanagement.dao;

import java.sql.SQLException;

public interface Dao<T> {
    Long insert(T entity) throws SQLException;

    Long update(T entity) throws SQLException;

    Long deleteById(Long id) throws SQLException;

    T findById(Long id) throws SQLException;
}
