package DaoValueTest;

import com.ImperioElevator.ordermanagement.OrderManagementApplication;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.valueobjects.CreateDateTime;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.UpdateDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//ToDO date :(
@SpringBootTest(classes = OrderManagementApplication.class)
@ActiveProfiles("test")

public class OrderDaoImplTest {

    @Autowired
    private OrderDaoImpl orderDao;

    @Autowired
    private DataSource dataSource;

    @Test
    public void testInsert() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        Order order = new Order(
                null,
                new User(new Id(1L), null, null),
                Status.NEW,
                new CreateDateTime(createdDate),
                new UpdateDateTime(updatedDate)
        );
        Long generatedId = orderDao.insert(order);
        order.setOrderId(new Id(generatedId));
        Order foundOrder = orderDao.findById(new Id(generatedId).getId());
        assertNotNull(foundOrder);
        assertNotNull(foundOrder.getCreatedDate());
        orderDao.deleteById(new Id(generatedId).getId());
    }

    @Test
    public void testUpdate() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        User user = new User(new Id(1L), null, null);
        Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
        Long generatedId = orderDao.insert(order);
        order.setOrderId(new Id(generatedId));

        order.setUserId(new User(new Id(2L), null, null));
        orderDao.update(order);

        Order foundOrder = orderDao.findById(new Id(generatedId).getId());
        assertNotNull(foundOrder);
        assertEquals(2L, foundOrder.getUserId().getUserId().getId());
        orderDao.deleteById(new Id(generatedId).getId());
    }

    @Test
    public void testDeleteById() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        User user = new User(new Id(1L), null, null);

        Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
        Long generatedId = orderDao.insert(order);
        order.setOrderId(new Id(generatedId));

        orderDao.deleteById(generatedId);
        Order foundOrder = orderDao.findById(generatedId);
        assertNull(foundOrder);
    }

    @Test
    public void testFindById() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        User user = new User(new Id(2L), null, null);

        Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
        Long generatedId = orderDao.insert(order);
        order.setOrderId(new Id(generatedId));

        Order foundOrder = orderDao.findById(new Id(generatedId).getId());
        assertNotNull(foundOrder);
        assertEquals(order.getOrderId().getId(), foundOrder.getOrderId().getId());
        assertEquals(order.getUserId().getUserId().getId(), foundOrder.getUserId().getUserId().getId());
        orderDao.deleteById(new Id(generatedId).getId());
    }

    @Test
    public void testFindPaginableOrderByCreatedDate() throws SQLException {
        clearOrders();

        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        for (int i = 0; i < 10; i++) {
            User user = new User(new Id(2L), null, null);
            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
            Long generatedId = orderDao.insert(order);
            order.setOrderId(new Id(generatedId));
            System.out.println("Inserted Order ID: " + generatedId);
        }

        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDate(createdDate, 5L, 2L);
        List<Order> orders = response.getItems();
        assertEquals(5, orders.size(), "Expected number of orders returned by query.");
        for (Order order : orders) {
            assertEquals(createdDate, order.getCreatedDate().getCreateDateTime());
        }

       // clearOrders();
    }



    @Test
    public void testFindPaginableOrderByUpdatedDate() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        for (int i = 0; i <= 10; i++) {
            User user = new User(new Id(2L), null, null);
            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
            Long generatedId = orderDao.insert(order);
            order.setOrderId(new Id(generatedId));
        }
        Paginable<Order> response = orderDao.findPaginableOrderByUpdatedDate(updatedDate, 5L, 1L);
        List<Order> orders = response.getItems();
        assertEquals(5, orders.size());
        for (Order order : orders) {
            assertEquals(updatedDate, order.getUpdatedDate().getUpdateDateTime());
        }
        clearOrders();
    }

    @Test
    public void testFindPaginableOrderByCreatedDateAndStatus() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        for (int i = 0; i <= 10; i++) {
            User user = new User(new Id(2L), null, null);
            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
            Long generatedId = orderDao.insert(order);
            order.setOrderId(new Id(generatedId));
        }
        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDateAndStatus(createdDate, Status.CLOSED, 5L, 2L);
        List<Order> orders = response.getItems();
        assertEquals(5, orders.size());
        for (Order order : orders) {
            assertEquals(createdDate, order.getCreatedDate().getCreateDateTime());
            assertEquals(Status.CLOSED, order.getOrderStatus());
        }
       // clearOrders();
    }

    @Test
    public void testFindPaginableOrderWahtIsLeftByCreatedDate() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        for (int i = 0; i <= 10; i++) {
            User user = new User(new Id(2L), null, null);
            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
            Long generatedId = orderDao.insert(order);
            order.setOrderId(new Id(generatedId));
        }
        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDate(createdDate, 6L, 2L);
        List<Order> orders = response.getItems();
        assertEquals(5, orders.size());
        for (Order order : orders) {
            assertEquals(createdDate, order.getCreatedDate().getCreateDateTime());
        }
        clearOrders();
    }

    @Test
    public void testFindPaginableGetThirdPageByCreatedDate() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        for (int i = 0; i <= 10; i++) {
            User user = new User(new Id(2L), null, null);
            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
            Long generatedId = orderDao.insert(order);
            order.setOrderId(new Id(generatedId));
        }
        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDate(createdDate, 3L, 3L);
        Paginable<Order> response2 = orderDao.findPaginableOrderByCreatedDate(createdDate, 3L, 4L);
        List<Order> orders = response.getItems();
        List<Order> orders2 = response2.getItems();
        assertEquals(3, orders.size());
        assertEquals(2, orders2.size());
        for (Order order : orders) {
            assertEquals(createdDate, order.getCreatedDate().getCreateDateTime());
        }
        for (Order order2 : orders2) {
            assertEquals(createdDate, order2.getCreatedDate().getCreateDateTime());
        }
        clearOrders();
    }

    @Test
    public void testPaginableToMuchRequestedOrders() throws SQLException {
        LocalDateTime createdDate = LocalDateTime.of(2024, 7, 1, 14, 30, 50);
        LocalDateTime updatedDate = LocalDateTime.of(2024, 7, 1, 15, 30, 50);
        for (int i = 0; i <= 10; i++) {
            User user = new User(new Id(2L), null, null);
            Order order = new Order(null, user, Status.IN_PROGRESS, new CreateDateTime(createdDate), new UpdateDateTime(updatedDate));
            Long generatedId = orderDao.insert(order);
            order.setOrderId(new Id(generatedId));
        }
        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDate(createdDate, 20L, 1L);
        List<Order> orders = response.getItems();
        assertEquals(11, orders.size(), "The number of orders is 11 as the requested page exceeds the total number of pages.");
        clearOrders();
    }

    private void clearOrders() throws SQLException {
        String sql = "DELETE FROM `order`";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}