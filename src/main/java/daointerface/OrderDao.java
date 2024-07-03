package daointerface;

import entity.Order;
import enumobects.Status;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderDao extends Dao<Order>{
    List<Order> findPaginableOrderByCreatedDate(LocalDateTime createdDate, int numberOfOrders) throws SQLException;
    List<Order> findPaginableOrderByUpdatedDate(LocalDateTime updatedDate, int numberOfOrders) throws SQLException;

    List<Order> findPaginableOrderByCreatedDateAndStatus(LocalDateTime updatedDate, Status status, int numberOfOrders) throws SQLException;


}
