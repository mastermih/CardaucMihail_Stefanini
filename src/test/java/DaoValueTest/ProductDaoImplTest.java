package DaoValueTest;

import com.ImperioElevator.ordermanagement.OrderManagementApplication;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.entity.Category;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.valueobjects.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = OrderManagementApplication.class)
@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductDaoImplTest {

    @Autowired
    private ProductDaoImpl productDao;

    @Autowired
    private Environment environment;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws SQLException {
        System.out.println("Active Profiles: " + Arrays.toString(environment.getActiveProfiles()));
        System.out.println("Datasource URL: " + dataSource.getConnection().getMetaData().getURL());
    }

    @Test
    public void testInsert() throws SQLException {
        Product product = new Product(
                null,
                new Price(100),
                new Width(50.00),
                new Height(20.00),
                new Depth(30.00),
                new Category(new Id(1L), null, null),
                new ProductBrand("Brand"),
                new ProductName("Name"),
                new ElectricityConsumption(10),
                new Description("Description")
        );
        Long generatedId = productDao.insert(product);
        product.setProductId(new Id(generatedId));
        Product foundProduct = productDao.findById(generatedId);
        assertNotNull(foundProduct);
        assertEquals(generatedId, foundProduct.getProductId().getId());
        assertEquals(product.getProductName().getProductName(), foundProduct.getProductName().getProductName());
        // productDao.deleteById(generatedId);
    }

    @Test
    public void testUpdate() throws SQLException {
        Product product = new Product(
                null,
                new Price(200),
                new Width(60.00),
                new Height(25.00),
                new Depth(35.00),
                new Category(new Id(1L), null, null),
                new ProductBrand("Brand"),
                new ProductName("Name"),
                new ElectricityConsumption(20),
                new Description("Description")
        );
        Long generatedId = productDao.insert(product);
        product.setProductId(new Id(generatedId));

        product.setProductName(new ProductName("Updated Name"));
        productDao.update(product);

        Product foundProduct = productDao.findById(generatedId);
        assertNotNull(foundProduct);
        assertEquals("Updated Name", foundProduct.getProductName().getProductName());
        productDao.deleteById(generatedId);
    }

    @Test
    public void testDeleteById() throws SQLException {
        Product product = new Product(
                null,
                new Price(300),
                new Width(70.00),
                new Height(30.00),
                new Depth(40.00),
                new Category(new Id(1L), null, null),
                new ProductBrand("Brand"),
                new ProductName("Name"),
                new ElectricityConsumption(30),
                new Description("Description")
        );
        Long generatedId = productDao.insert(product);
        productDao.deleteById(generatedId);
        Product foundProduct = productDao.findById(generatedId);
        assertNull(foundProduct);
    }

    @Test
    public void testFindById() throws SQLException {
        Product product = new Product(
                null,
                new Price(400),
                new Width(80.00),
                new Height(35.00),
                new Depth(45.00),
                new Category(new Id(1L), null, null),
                new ProductBrand("Brand"),
                new ProductName("Name"),
                new ElectricityConsumption(40),
                new Description("Description")
        );
        Long generatedId = productDao.insert(product);
        product.setProductId(new Id(generatedId));

        Product foundProduct = productDao.findById(generatedId);
        assertNotNull(foundProduct);
        assertEquals(product.getProductId().getId(), foundProduct.getProductId().getId());
        assertEquals(product.getProductName().getProductName(), foundProduct.getProductName().getProductName());
        productDao.deleteById(generatedId);
    }

    @SpringBootTest(classes = OrderManagementApplication.class)
    @ActiveProfiles("test")
    public static class OrderDaoImplTest {

        @Autowired
        private OrderDaoImpl orderDao;

        @Autowired
        private DataSource dataSource;

        @Test
        public void testInsert() throws SQLException {
            LocalDate localDate = LocalDate.of(2024, 7, 1);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            com.ImperioElevator.ordermanagement.entity.Order order = new com.ImperioElevator.ordermanagement.entity.Order(
                    null,
                    new User(new Id(1L), null, null),
                    Status.NEW,
                    new CreateDateTime(localDateTime),
                    new UpdateDateTime(localDateTime)
            );
            Long generatedId = orderDao.insert(order);
            order.setOrderId(new Id(generatedId));
            com.ImperioElevator.ordermanagement.entity.Order foundOrder = orderDao.findById(new Id(generatedId).getId());
            assertNotNull(foundOrder);
            assertNotNull(foundOrder.getCreatedDate());
            orderDao.deleteById(new Id(generatedId).getId());
        }

        @Test
        public void testUpdate() throws SQLException {
            LocalDate localDate = LocalDate.of(2024, 7, 1);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            User user = new User(new Id(1L), null, null);
            com.ImperioElevator.ordermanagement.entity.Order order = new com.ImperioElevator.ordermanagement.entity.Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime));
            Long generatedId = orderDao.insert(order);
            order.setOrderId(new Id(generatedId));

            order.setUserId(new User(new Id(2L), null, null));
            orderDao.update(order);

            com.ImperioElevator.ordermanagement.entity.Order foundOrder = orderDao.findById(new Id(generatedId).getId());
            assertNotNull(foundOrder);
            assertEquals(2L, foundOrder.getUserId().getUserId().getId());
            orderDao.deleteById(new Id(generatedId).getId());
        }

        @Test
        public void testDeleteById() throws SQLException {
            LocalDate localDate = LocalDate.of(2024, 7, 1);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            User user = new User(new Id(1L), null, null);

            com.ImperioElevator.ordermanagement.entity.Order order = new com.ImperioElevator.ordermanagement.entity.Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime));
            Long generatedId = orderDao.insert(order);
            order.setOrderId(new Id(generatedId));

            orderDao.deleteById(generatedId);
            com.ImperioElevator.ordermanagement.entity.Order foundOrder = orderDao.findById(generatedId);
            assertNull(foundOrder);
        }

        @Test
        public void testFindById() throws SQLException {
            LocalDate localDate = LocalDate.of(2024, 7, 1);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            User user = new User(new Id(2L), null, null);

            com.ImperioElevator.ordermanagement.entity.Order order = new com.ImperioElevator.ordermanagement.entity.Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime));
            Long generatedId = orderDao.insert(order);
            order.setOrderId(new Id(generatedId));

            com.ImperioElevator.ordermanagement.entity.Order foundOrder = orderDao.findById(new Id(generatedId).getId());
            assertNotNull(foundOrder);
            assertEquals(order.getOrderId().getId(), foundOrder.getOrderId().getId());
            assertEquals(order.getUserId().getUserId().getId(), foundOrder.getUserId().getUserId().getId());
            orderDao.deleteById(new Id(generatedId).getId());
        }

        @Test
        public void testFindPaginableOrderByCreatedDate() throws SQLException {
            clearOrders();

            LocalDate localDate = LocalDate.of(2024, 7, 1);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            for (int i = 0; i < 10; i++) {
                User user = new User(new Id(2L), null, null);
                com.ImperioElevator.ordermanagement.entity.Order order = new com.ImperioElevator.ordermanagement.entity.Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime));
                Long generatedId = orderDao.insert(order);
                order.setOrderId(new Id(generatedId));
                System.out.println("Inserted Order ID: " + generatedId);
            }

            Paginable<com.ImperioElevator.ordermanagement.entity.Order> response = orderDao.findPaginableOrderByCreatedDate(localDateTime, localDateTime, 5L, 2L);
            List<com.ImperioElevator.ordermanagement.entity.Order> orders = response.getItems();
            assertEquals(5, orders.size(), "Expected number of orders returned by query.");
            for (com.ImperioElevator.ordermanagement.entity.Order order : orders) {
                assertEquals(localDateTime, order.getCreatedDate().getCreateDateTime());
            }

            clearOrders();
        }

        @Test
        public void testFindPaginableOrderByUpdatedDate() throws SQLException {
            LocalDate localDate = LocalDate.of(2024, 7, 1);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            for (int i = 0; i <= 10; i++) {
                User user = new User(new Id(2L), null, null);
                com.ImperioElevator.ordermanagement.entity.Order order = new com.ImperioElevator.ordermanagement.entity.Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime));
                Long generatedId = orderDao.insert(order);
                order.setOrderId(new Id(generatedId));
            }
            Paginable<com.ImperioElevator.ordermanagement.entity.Order> response = orderDao.findPaginableOrderByUpdatedDate(localDateTime, localDateTime, 5L, 1L);
            List<com.ImperioElevator.ordermanagement.entity.Order> orders = response.getItems();
            assertEquals(5, orders.size());
            for (com.ImperioElevator.ordermanagement.entity.Order order : orders) {
                assertEquals(localDateTime, order.getUpdatedDate().getUpdateDateTime());
            }
            clearOrders();
        }

        @Test
        public void testFindPaginableOrderByCreatedDateAndStatus() throws SQLException {
            LocalDate localDate = LocalDate.of(2024, 7, 1);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            for (int i = 0; i <= 10; i++) {
                User user = new User(new Id(2L), null, null);
                com.ImperioElevator.ordermanagement.entity.Order order = new com.ImperioElevator.ordermanagement.entity.Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime));
                Long generatedId = orderDao.insert(order);
                order.setOrderId(new Id(generatedId));
            }
            Paginable<com.ImperioElevator.ordermanagement.entity.Order> response = orderDao.findPaginableOrderByCreatedDateAndStatus(localDateTime, localDateTime, Status.CLOSED, 5L, 2L);
            List<com.ImperioElevator.ordermanagement.entity.Order> orders = response.getItems();
            assertEquals(5, orders.size());
            for (com.ImperioElevator.ordermanagement.entity.Order order : orders) {
                assertEquals(localDateTime, order.getCreatedDate().getCreateDateTime());
                assertEquals(Status.CLOSED, order.getOrderStatus());
            }
            clearOrders();
        }

        @Test
        public void testFindPaginableOrderWahtIsLeftByCreatedDate() throws SQLException {
            LocalDate localDate = LocalDate.of(2024, 7, 1);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            for (int i = 0; i <= 10; i++) {
                User user = new User(new Id(2L), null, null);
                com.ImperioElevator.ordermanagement.entity.Order order = new com.ImperioElevator.ordermanagement.entity.Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime));
                Long generatedId = orderDao.insert(order);
                order.setOrderId(new Id(generatedId));
            }
            Paginable<com.ImperioElevator.ordermanagement.entity.Order> response = orderDao.findPaginableOrderByCreatedDate(localDateTime, localDateTime, 6L, 2L);
            List<com.ImperioElevator.ordermanagement.entity.Order> orders = response.getItems();
            assertEquals(5, orders.size());
            for (com.ImperioElevator.ordermanagement.entity.Order order : orders) {
                assertEquals(localDateTime, order.getCreatedDate().getCreateDateTime());
            }
            clearOrders();
        }

        @Test
        public void testFindPaginableGetThirdPageByCreatedDate() throws SQLException {
            LocalDate localDate = LocalDate.of(2024, 7, 1);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            for (int i = 0; i <= 10; i++) {
                User user = new User(new Id(2L), null, null);
                com.ImperioElevator.ordermanagement.entity.Order order = new com.ImperioElevator.ordermanagement.entity.Order(null, user, Status.CLOSED, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime));
                Long generatedId = orderDao.insert(order);
                order.setOrderId(new Id(generatedId));
            }
            Paginable<com.ImperioElevator.ordermanagement.entity.Order> response = orderDao.findPaginableOrderByCreatedDate(localDateTime, localDateTime, 3L, 3L);
            Paginable<com.ImperioElevator.ordermanagement.entity.Order> response2 = orderDao.findPaginableOrderByCreatedDate(localDateTime, localDateTime, 3L, 4L);
            List<com.ImperioElevator.ordermanagement.entity.Order> orders = response.getItems();
            List<com.ImperioElevator.ordermanagement.entity.Order> orders2 = response2.getItems();
            assertEquals(3, orders.size());
            assertEquals(2, orders2.size());
            for (com.ImperioElevator.ordermanagement.entity.Order order : orders) {
                assertEquals(localDateTime, order.getCreatedDate().getCreateDateTime());
            }
            for (com.ImperioElevator.ordermanagement.entity.Order order2 : orders2) {
                assertEquals(localDateTime, order2.getCreatedDate().getCreateDateTime());
            }
            clearOrders();
        }

        @Test
        public void testPaginableToMuchRequestedOrders() throws SQLException {
            LocalDate localDate = LocalDate.of(2024, 7, 1);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            for (int i = 0; i <= 10; i++) {
                User user = new User(new Id(2L), null, null);
                com.ImperioElevator.ordermanagement.entity.Order order = new com.ImperioElevator.ordermanagement.entity.Order(null, user, Status.IN_PROGRESS, new CreateDateTime(localDateTime), new UpdateDateTime(localDateTime));
                Long generatedId = orderDao.insert(order);
                order.setOrderId(new Id(generatedId));
            }
            Paginable<com.ImperioElevator.ordermanagement.entity.Order> response = orderDao.findPaginableOrderByCreatedDate(localDateTime, localDateTime, 20L, 1L);
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
}
