package DaoValueTest;

import DaoImplementation.OrderDaoImpl;
import DaoImplementation.ProductDaoImpl;
import Entity.Order;
import JDBC.DatabaseConnectionDemo;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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
        Order order = new Order(13L, 1L);
        orderDao.insert(order);

        Order foundOrder = orderDao.findById(13L);
        assertNotNull(foundOrder);
        assertEquals(order.getOrderId(), foundOrder.getOrderId());
        assertEquals(order.getUserId(), foundOrder.getUserId());
        orderDao.deleteById(13L);
    }

    @Test
    public void testUpdate() throws SQLException {
        Order order = new Order(14L, 1L);
        orderDao.insert(order);

        order.setUserId(2);
        orderDao.update(order);

        Order foundOrder = orderDao.findById(14);
        assertNotNull(foundOrder);
        assertEquals(2L, foundOrder.getUserId());
        orderDao.deleteById(14L);

    }

    @Test
    public void testDeleteById() throws SQLException {
        Order order = new Order(15L, 111L);
        orderDao.insert(order);

        orderDao.deleteById(15);
        Order foundOrder = orderDao.findById(15);
        assertNull(foundOrder);
    }

    @Test
    public void testFindById() throws SQLException {
        Order order = new Order(16L, 8L);
        orderDao.insert(order);

        Order foundOrder = orderDao.findById(16);
        assertNotNull(foundOrder);
        assertEquals(order.getOrderId(), foundOrder.getOrderId());
        assertEquals(order.getUserId(), foundOrder.getUserId());
        orderDao.deleteById(16L);

    }
}