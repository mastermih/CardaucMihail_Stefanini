//package DaoValueTest;
//
//import com.ImperioElevator.ordermanagement.OrderManagementApplication;
//import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
//import com.ImperioElevator.ordermanagement.entity.Order;
//import com.ImperioElevator.ordermanagement.entity.Paginable;
//import com.ImperioElevator.ordermanagement.entity.User;
//import com.ImperioElevator.ordermanagement.enumobects.Status;
//import com.ImperioElevator.ordermanagement.valueobjects.CreateDateTime;
//import com.ImperioElevator.ordermanagement.valueobjects.Id;
//import com.ImperioElevator.ordermanagement.valueobjects.UpdateDateTime;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(classes = OrderManagementApplication.class)
//@ActiveProfiles("test")
//public class OrderDaoImplTest {
//
//    @Autowired
//    private OrderDaoImpl orderDao;
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Test
//    public void testInsert() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//        Order order = new Order(
//                null,
//                new User(new Id(1L), null, null),
//                Status.NEW,
//                new CreateDateTime(sqlDate),
//                new UpdateDateTime(sqlDate)
//        );
//        Long generatedId = orderDao.insert(order);
//        order.setOrderId(new Id(generatedId));
//        Order foundOrder = orderDao.findById(new Id(generatedId).getId());
//        assertNotNull(foundOrder);
//        assertNotNull(foundOrder.getCreatedDate());
//        orderDao.deleteById(new Id(generatedId).getId());
//    }
//
//    @Test
//    public void testUpdate() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//        User user = new User(new Id(1L), null, null);
//        Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate));
//        Long generatedId = orderDao.insert(order);
//        order.setOrderId(new Id(generatedId));
//
//        order.setUserId(new User(new Id(2L), null, null));
//        orderDao.update(order);
//
//        Order foundOrder = orderDao.findById(new Id(generatedId).getId());
//        assertNotNull(foundOrder);
//        assertEquals(2L, foundOrder.getUserId().getUserId().getId());
//        orderDao.deleteById(new Id(generatedId).getId());
//    }
//
//    @Test
//    public void testDeleteById() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//        User user = new User(new Id(1L), null, null);
//
//        Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate));
//        Long generatedId = orderDao.insert(order);
//        order.setOrderId(new Id(generatedId));
//
//        orderDao.deleteById(generatedId);
//        Order foundOrder = orderDao.findById(generatedId);
//        assertNull(foundOrder);
//    }
//
//    @Test
//    public void testFindById() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//        User user = new User(new Id(2L), null, null);
//
//        Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate));
//        Long generatedId = orderDao.insert(order);
//        order.setOrderId(new Id(generatedId));
//
//        Order foundOrder = orderDao.findById(new Id(generatedId).getId());
//        assertNotNull(foundOrder);
//        assertEquals(order.getOrderId().getId(), foundOrder.getOrderId().getId());
//        assertEquals(order.getUserId().getUserId().getId(), foundOrder.getUserId().getUserId().getId());
//        orderDao.deleteById(new Id(generatedId).getId());
//    }
//
//    @Test
//    public void testFindPaginableOrderByCreatedDate() throws SQLException {
//        clearOrders();
//
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//        for (int i = 0; i < 10; i++) {
//            User user = new User(new Id(2L), null, null);
//            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate));
//            Long generatedId = orderDao.insert(order);
//            order.setOrderId(new Id(generatedId));
//            System.out.println("Inserted Order ID: " + generatedId);
//        }
//
//        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDate(sqlDate, sqlDate, 5L, 2L);
//        List<Order> orders = response.getItems();
//        assertEquals(5, orders.size(), "Expected number of orders returned by query.");
//        for (Order order : orders) {
//            assertEquals(sqlDate, order.getCreatedDate().getCreateDateTime());
//        }
//
//        clearOrders();
//    }
//
//    @Test
//    public void testFindPaginableOrderByUpdatedDate() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//        for (int i = 0; i <= 10; i++) {
//            User user = new User(new Id(2L), null, null);
//            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate));
//            Long generatedId = orderDao.insert(order);
//            order.setOrderId(new Id(generatedId));
//        }
//        Paginable<Order> response = orderDao.findPaginableOrderByUpdatedDate(sqlDate, sqlDate, 5L, 1L);
//        List<Order> orders = response.getItems();
//        assertEquals(5, orders.size());
//        for (Order order : orders) {
//            assertEquals(sqlDate, order.getUpdatedDate().getUpdateDateTime());
//        }
//        clearOrders();
//    }
//
//    @Test
//    public void testFindPaginableOrderByCreatedDateAndStatus() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//        for (int i = 0; i <= 10; i++) {
//            User user = new User(new Id(2L), null, null);
//            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate));
//            Long generatedId = orderDao.insert(order);
//            order.setOrderId(new Id(generatedId));
//        }
//        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDateAndStatus(sqlDate, sqlDate, Status.CLOSED, 5L, 2L);
//        List<Order> orders = response.getItems();
//        assertEquals(5, orders.size());
//        for (Order order : orders) {
//            assertEquals(sqlDate, order.getCreatedDate().getCreateDateTime());
//            assertEquals(Status.CLOSED, order.getOrderStatus());
//        }
//        clearOrders();
//    }
//
//    @Test
//    public void testFindPaginableOrderWahtIsLeftByCreatedDate() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//        for (int i = 0; i <= 10; i++) {
//            User user = new User(new Id(2L), null, null);
//            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate));
//            Long generatedId = orderDao.insert(order);
//            order.setOrderId(new Id(generatedId));
//        }
//        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDate(sqlDate, sqlDate, 6L, 2L);
//        List<Order> orders = response.getItems();
//        assertEquals(5, orders.size());
//        for (Order order : orders) {
//            assertEquals(sqlDate, order.getCreatedDate().getCreateDateTime());
//        }
//        clearOrders();
//    }
//
//    @Test
//    public void testFindPaginableGetThirdPageByCreatedDate() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//        for (int i = 0; i <= 10; i++) {
//            User user = new User(new Id(2L), null, null);
//            Order order = new Order(null, user, Status.CLOSED, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate));
//            Long generatedId = orderDao.insert(order);
//            order.setOrderId(new Id(generatedId));
//        }
//        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDate(sqlDate, sqlDate, 3L, 3L);
//        Paginable<Order> response2 = orderDao.findPaginableOrderByCreatedDate(sqlDate, sqlDate, 3L, 4L);
//        List<Order> orders = response.getItems();
//        List<Order> orders2 = response2.getItems();
//        assertEquals(3, orders.size());
//        assertEquals(2, orders2.size());
//        for (Order order : orders) {
//            assertEquals(sqlDate, order.getCreatedDate().getCreateDateTime());
//        }
//        for (Order order2 : orders2) {
//            assertEquals(sqlDate, order2.getCreatedDate().getCreateDateTime());
//        }
//        clearOrders();
//    }
//
//    @Test
//    public void testPaginableToMuchRequestedOrders() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//        for (int i = 0; i <= 10; i++) {
//            User user = new User(new Id(2L), null, null);
//            Order order = new Order(null, user, Status.IN_PROGRESS, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate));
//            Long generatedId = orderDao.insert(order);
//            order.setOrderId(new Id(generatedId));
//        }
//        Paginable<Order> response = orderDao.findPaginableOrderByCreatedDate(sqlDate, sqlDate, 20L, 1L);
//        List<Order> orders = response.getItems();
//        assertEquals(11, orders.size(), "The number of orders is 11 as the requested page exceeds the total number of pages.");
//        clearOrders();
//    }
//
//    private void clearOrders() throws SQLException {
//        String sql = "DELETE FROM `order`";
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//}
//
