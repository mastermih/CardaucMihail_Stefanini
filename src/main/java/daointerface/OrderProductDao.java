package daointerface;

import entity.OrderProduct;
import java.sql.SQLException;
import java.util.List;

public interface OrderProductDao {
    long insert(OrderProduct orderProduct) throws SQLException;
    long update(OrderProduct orderProduct) throws SQLException;
    long deleteById(long orderId, long productId) throws SQLException;
    OrderProduct findById(long orderId, long productId) throws SQLException;
}
