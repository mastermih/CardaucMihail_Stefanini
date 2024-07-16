//package DaoValueTest;
//
//import com.ImperioElevator.ordermanagement.OrderManagementApplication;
//import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderProductDaoImpl;
//import com.ImperioElevator.ordermanagement.entity.Order;
//import com.ImperioElevator.ordermanagement.entity.OrderProduct;
//import com.ImperioElevator.ordermanagement.entity.Product;
//import com.ImperioElevator.ordermanagement.enumobects.Status;
//import com.ImperioElevator.ordermanagement.valueobjects.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.sql.Date;
//import java.sql.SQLException;
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(classes = OrderManagementApplication.class)
//@ActiveProfiles("test")
//public class OrderProductDaoImplTest {
//
//    @Autowired
//    private OrderProductDaoImpl orderProductDao;
//
//    @Test
//    void testInsert() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//
//        OrderProduct orderProduct = new OrderProduct(
//                new Order(new Id(1L), null, Status.NEW, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate)),
//                new Product(new Id(1L), null, null, null, null, null, null, null, null, null),
//                new Quantity(1),
//                new Price(323)
//        );
//        orderProductDao.insert(orderProduct);
//        OrderProduct foundOrderProduct = orderProductDao.findById(1L, 1L);
//        assertNotNull(foundOrderProduct);
//
//        orderProductDao.deleteById(1L, 1L);
//    }
//
//    @Test
//    public void testUpdate() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//
//        OrderProduct orderProduct = new OrderProduct(
//                new Order(new Id(1L), null, Status.NEW, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate)),
//                new Product(new Id(1L), null, null, null, null, null, null, null, null, null),
//                new Quantity(1),
//                new Price(323)
//        );
//        Long orderId = orderProduct.getOrderId().getOrderId().getId();
//        Long productId = orderProduct.getProductId().getProductId().getId();
//        orderProductDao.insert(orderProduct);
//        orderProduct.setQuantity(new Quantity(2));
//        orderProduct.setPriceOrder(new Price(500));
//
//        orderProductDao.update(orderProduct);
//        OrderProduct foundOrderProduct = orderProductDao.findById(orderId, productId);
//        assertNotNull(foundOrderProduct);
//        assertEquals(orderProduct.getQuantity().getQuantity(), foundOrderProduct.getQuantity().getQuantity());
//        assertEquals(orderProduct.getPriceOrder().getPrice(), foundOrderProduct.getPriceOrder().getPrice());
//
//        orderProductDao.deleteById(orderId, productId);
//    }
//
//    @Test
//    public void testDeleteById() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//
//        OrderProduct orderProduct = new OrderProduct(
//                new Order(new Id(1L), null, Status.NEW, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate)),
//                new Product(new Id(1L), null, null, null, null, null, null, null, null, null),
//                new Quantity(1),
//                new Price(323)
//        );
//        Long orderId = orderProduct.getOrderId().getOrderId().getId();
//        Long productId = orderProduct.getProductId().getProductId().getId();
//        orderProductDao.insert(orderProduct);
//        orderProductDao.deleteById(orderId, productId);
//        OrderProduct foundOrderProduct = orderProductDao.findById(orderId, productId);
//        assertNull(foundOrderProduct);
//    }
//
//    @Test
//    public void testFindById() throws SQLException {
//        LocalDate localDate = LocalDate.of(2024, 7, 1);
//        Date sqlDate = Date.valueOf(localDate);
//
//        OrderProduct orderProduct = new OrderProduct(
//                new Order(new Id(1L), null, Status.NEW, new CreateDateTime(sqlDate), new UpdateDateTime(sqlDate)),
//                new Product(new Id(1L), null, null, null, null, null, null, null, null, null),
//                new Quantity(1),
//                new Price(323)
//        );
//        Long orderId = orderProduct.getOrderId().getOrderId().getId();
//        Long productId = orderProduct.getProductId().getProductId().getId();
//        orderProductDao.insert(orderProduct);
//        OrderProduct foundOrderProduct = orderProductDao.findById(orderId, productId);
//        assertNotNull(foundOrderProduct);
//
//        orderProductDao.deleteById(orderId, productId);
//    }
//}
