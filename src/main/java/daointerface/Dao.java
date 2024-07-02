package daointerface;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    long  insert(T entity) throws SQLException;
    long  update(T entity) throws SQLException;
    long  deleteById(long id) throws SQLException;
    T findById(long id) throws SQLException;
   /// List<T> findAll() throws SQLException;
}
