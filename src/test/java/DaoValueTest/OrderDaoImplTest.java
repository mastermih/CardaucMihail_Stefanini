package DaoValueTest;

import daoimplementation.OrderDaoImpl;
import entity.Order;
import entity.User;
import enumobects.Status;
import jdbc.DatabaseConnectionDemo;
import org.junit.jupiter.api.*;
import valueobjects.CreateDateTime;
import valueobjects.Id;
import valueobjects.UpdateDateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDaoImplTest {

    private static Connection connection;
    private static OrderDaoImpl orderDao;

    @BeforeAll
    public static void setUp() {
        connection = DatabaseConnectionDemo.open();
        orderDao = new OrderDaoImpl(connection);
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testInsert() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
            Order order = new Order(
                    null,
                    new User(new Id(1), null, null),
                    Status.NEW,
                    new CreateDateTime(createdDate),
                    new UpdateDateTime(updatedDate)
                   );
            long generatedId = orderDao.insert(order);

        Order foundOrder = orderDao.findById(generatedId);
        assertNotNull(foundOrder);
        assertNotNull(foundOrder.getCreatedDate());
        orderDao.deleteById(generatedId);

    }

    @Test
    public void testUpdate() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        User user = new User(new Id(1), null, null);
        Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
        long generatedId = orderDao.insert(order);
        order.setOrderId(new Id(generatedId));

        order.setUserId(new User(new Id(2), null, null));  // Update the userId
        orderDao.update(order);

        Order foundOrder = orderDao.findById(generatedId);
        assertNotNull(foundOrder);
        assertEquals(2L, foundOrder.getUserId().getUserId().getId());
        orderDao.deleteById(generatedId);

    }

    @Test
    public void testDeleteById() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        User user = new User(new Id(1), null, null);

        Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
        long generatedId = orderDao.insert(order);
        order.setOrderId(new Id(generatedId));

        orderDao.deleteById(generatedId);
        Order foundOrder = orderDao.findById(generatedId);
        assertNull(foundOrder);
    }

    @Test
    public void testFindById() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        User user = new User(new Id(2), null, null);

        Order order = new Order(null, user,Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
        long generatedId = orderDao.insert(order);
        order.setOrderId(new Id(generatedId));

        Order foundOrder = orderDao.findById(generatedId);
        assertNotNull(foundOrder);
        assertEquals(order.getOrderId().getId(), foundOrder.getOrderId().getId());
        assertEquals(order.getUserId().getUserId().getId(), foundOrder.getUserId().getUserId().getId());
        orderDao.deleteById(generatedId);

        }
        @Test
    public void testFindPaginableOrderByCreatedDate() throws SQLException {
            LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
            LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        for(int i = 0; i<=10; i++){

                User user = new User(new Id(2), null, null);

                Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
                long generatedId = orderDao.insert(order);
                order.setOrderId(new Id(generatedId));
            }
            List<Order> orders = orderDao.findPaginableOrderByCreatedDate(createdDate, 5);
            assertEquals(5, orders.size());
            for(Order order: orders) {
                assertEquals(createdDate, order.getCreatedDate().getCreateDateTime());
            }
            clearOrders();

        }
    @Test
    public void testFindPaginableOrderByUpdatedDate() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        for(int i = 0; i<=10; i++){

            User user = new User(new Id(2), null, null);

            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
            long generatedId = orderDao.insert(order);
            order.setOrderId(new Id(generatedId));
        }
        List<Order> orders = orderDao.findPaginableOrderByCreatedDate(createdDate, 5);
        assertEquals(5, orders.size());
        for(Order order: orders) {
            assertEquals(updatedDate, order.getUpdatedDate().getUpdateDateTime());
        }
        clearOrders();

    }
    @Test
    public void testFindPaginableOrderByCreatedDateAndStatus()throws SQLException
    {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        for(int i = 0; i<=10; i++){

            User user = new User(new Id(2), null, null);

            Order order = new Order(null, user, Status.IN_PROGRES, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
            long generatedId = orderDao.insert(order);
            order.setOrderId(new Id(generatedId));
        }
        List<Order> orders = orderDao.findPaginableOrderByCreatedDateAndStatus(createdDate, Status.IN_PROGRES, 5);
        assertEquals(5, orders.size());
        for(Order order: orders) {
            assertEquals(createdDate, order.getCreatedDate().getCreateDateTime());
            assertEquals(Status.IN_PROGRES, order.getOrderStatus());
        }
        clearOrders();
    }


    private void clearOrders() throws SQLException {
        String sql = "DELETE FROM `order`";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }
}
