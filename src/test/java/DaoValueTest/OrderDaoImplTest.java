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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = OrderManagementApplication.class)
@ActiveProfiles("test")
public class OrderDaoImplTest {

    @Autowired
    private OrderDaoImpl orderDao;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws SQLException {
        clearOrders();
    }

    @Test
    public void testInsert() throws SQLException {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 7, 1, 0, 0);
        Order order = new Order(
                null,
                new User(new Id(1L), null, null),
                Status.NEW,
                new CreateDateTime(localDateTime),
                new UpdateDateTime(localDateTime),
                new ArrayList<>()

        );

        Long generatedId = orderDao.insert(order);
        Order foundOrder = orderDao.findById(generatedId);
        assertNotNull(foundOrder);
        assertNotNull(foundOrder.createdDate());
    }

    @Test
    public void testUpdate() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        User user = new User(new Id(1L), null, null);
        Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime), new ArrayList<>());
        Long generatedId = orderDao.insert(order);
        Order foundOrder = orderDao.findById(generatedId);

        foundOrder = new Order(new Id(generatedId), new User(new Id(2L), null, null), foundOrder.orderStatus(), foundOrder.createdDate(), foundOrder.updatedDate(), new ArrayList<>());
        orderDao.update(foundOrder);

        Order updatedOrder = orderDao.findById(generatedId);
        assertNotNull(updatedOrder);
        assertEquals(2L, updatedOrder.userId().userId().id());
    }

    @Test
    public void testDeleteById() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        User user = new User(new Id(1L), null, null);
        Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime), new ArrayList<>());
        Long generatedId = orderDao.insert(order);

        orderDao.deleteById(generatedId);
        Order foundOrder = orderDao.findById(generatedId);
        assertNull(foundOrder);
    }

    @Test
    public void testFindPaginableOrderByCreatedDate() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        for (int i = 0; i < 10; i++) {
            User user = new User(new Id(2L), null, null);
            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime), new ArrayList<>());
            orderDao.insert(order);
        }

        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDate(localDateTime, localDateTime, 5L, 2L);
        List<Order> orders = response.getItems();
        assertEquals(5, orders.size());
        for (Order order : orders) {
            assertEquals(localDateTime, order.createdDate().createDateTime());
        }
    }

    @Test
    public void testFindPaginableOrderByUpdatedDate() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        for (int i = 0; i <= 10; i++) {
            User user = new User(new Id(2L), null, null);
            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime), new ArrayList<>());
            orderDao.insert(order);
        }
        Paginable<Order> response = orderDao.findPaginableOrderByUpdatedDate(localDateTime, localDateTime, 5L, 1L);
        List<Order> orders = response.getItems();
        assertEquals(5, orders.size());
        for (Order order : orders) {
            assertEquals(localDateTime, order.updatedDate().updateDateTime());
        }
    }

    @Test
    public void testFindPaginableOrderByCreatedDateAndStatus() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        for (int i = 0; i <= 10; i++) {
            User user = new User(new Id(2L), null, null);
            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime), new ArrayList<>());
            orderDao.insert(order);
        }
        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDateAndStatus(localDateTime, localDateTime, Status.CLOSED, 5L, 2L);
        List<Order> orders = response.getItems();
        assertEquals(5, orders.size());
        for (Order order : orders) {
            assertEquals(localDateTime, order.createdDate().createDateTime());
            assertEquals(Status.CLOSED, order.orderStatus());
        }
    }

    @Test
    public void testFindPaginableOrderWahtIsLeftByCreatedDate() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        for (int i = 0; i <= 10; i++) {
            User user = new User(new Id(2L), null, null);
            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime), new ArrayList<>());
            orderDao.insert(order);
        }
        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDate(localDateTime, localDateTime, 6L, 2L);
        List<Order> orders = response.getItems();
        assertEquals(5, orders.size());
        for (Order order : orders) {
            assertEquals(localDateTime, order.createdDate().createDateTime());
        }
    }

    @Test
    public void testFindPaginableGetThirdPageByCreatedDate() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        for (int i = 0; i <= 10; i++) {
            User user = new User(new Id(2L), null, null);
            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime), new ArrayList<>());
            orderDao.insert(order);
        }
        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDate(localDateTime, localDateTime, 3L, 3L);
        Paginable<Order> response2 = orderDao.findPaginableOrderByCreatedDate(localDateTime, localDateTime, 3L, 4L);
        List<Order> orders = response.getItems();
        List<Order> orders2 = response2.getItems();
        assertEquals(3, orders.size());
        assertEquals(2, orders2.size());
        for (Order order : orders) {
            assertEquals(localDateTime, order.createdDate().createDateTime());
        }
        for (Order order2 : orders2) {
            assertEquals(localDateTime, order2.createdDate().createDateTime());
        }
    }

    @Test
    public void testPaginableToMuchRequestedOrders() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        for (int i = 0; i <= 10; i++) {
            User user = new User(new Id(2L), null, null);
            Order order = new Order(null, user, Status.IN_PROGRESS, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime), new ArrayList<>());
            orderDao.insert(order);
        }
        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDate(localDateTime, localDateTime, 20L, 1L);
        List<Order> orders = response.getItems();
        assertEquals(11, orders.size(), "The number of orders is 11 as the requested page exceeds the total number of pages.");
    }

    private void clearOrders() throws SQLException {
        String sql = "DELETE FROM client_order";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }
}
