package DaoWork;

import java.sql.SQLException;

public interface DaoMain<T> {
    void insert(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    void deleteById(int id) throws SQLException;
    T findById(int id) throws SQLException;
}
