package DaoValueTest;

import com.ImperioElevator.ordermanagement.OrderManagementApplication;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderProductDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Status;
import com.ImperioElevator.ordermanagement.valueobjects.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = OrderManagementApplication.class)
@ActiveProfiles("test")
public class OrderProductDaoImplTest {

    @Autowired
    private OrderProductDaoImpl orderProductDao;

    @Test
    void testInsert() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 7, 1);

        Order order = new Order(
                new Id(1L),
                null,
                Status.NEW,
                new CreateDateTime(localDate.atStartOfDay()),
                new UpdateDateTime(localDate.atStartOfDay()),
                new ArrayList<>()
        );

        Product product = new Product(
                new Id(1L), null, null, null, null, null, null, null, null, null, null, null
        );

        OrderProduct orderProduct = new OrderProduct(
                new Id(1L),
                order,
                product,
                new Quantity(3),
                new Price(323),
                new Id(null)
        );

        orderProductDao.insert(orderProduct);
        OrderProduct foundOrderProduct = orderProductDao.findById(order.orderId().id(), product.productName().productName());
        assertNotNull(foundOrderProduct);

        orderProductDao.deleteById(order.orderId().id(), product.productName().productName());
    }

    @Test
    public void testUpdate() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        //Date sqlDate = Date.valueOf(localDate);

        Order order = new Order(
                new Id(1L),
                null,
                Status.NEW,
                new CreateDateTime(localDate.atStartOfDay()),
                new UpdateDateTime(localDate.atStartOfDay()),
                new ArrayList<>()
        );

        Product product = new Product(
                new Id(1L), null, null, null, null, null, null, null, null, null, null, null
        );

        OrderProduct orderProduct = new OrderProduct(
                new Id(1L),
                order,
                product,
                new Quantity(3),
                new Price(323),
                new Id(2L)
        );

        Long orderId = orderProduct.orderId().id();
        String productName = orderProduct.product().productName().productName();
        orderProductDao.insert(orderProduct);
        //orderProduct.quantity(new Quantity(2));
        //orderProduct.priceOrder(new Price(500));
        OrderProduct foundOrder = new OrderProduct(new Id(1L), order, product, new Quantity(42), new Price(42L), new Id(42L));
        orderProductDao.update(foundOrder);

       // orderProductDao.update(foundOrder);
        OrderProduct foundOrderProduct = orderProductDao.findById(orderId, productName);
        assertNotNull(foundOrderProduct);
        assertNotEquals(orderProduct.quantity().quantity(), foundOrderProduct.quantity().quantity());
        assertNotEquals(orderProduct.priceOrder().price(), foundOrderProduct.priceOrder().price());

        orderProductDao.deleteById(orderId, productName);
    }

    @Test
    public void testDeleteById() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        //Date sqlDate = Date.valueOf(localDate);

        Order order = new Order(
                new Id(1L),
                null,
                Status.NEW,
                new CreateDateTime(localDate.atStartOfDay()),
                new UpdateDateTime(localDate.atStartOfDay()),
                new ArrayList<>()
        );

        Product product = new Product(
                new Id(1L), null, null, null, null, null, null, null, null, null, null, null
        );

        OrderProduct orderProduct = new OrderProduct(
                new Id(1L),
                order,
                product,
                new Quantity(3),
                new Price(323),
                new Id(null)
        );
        Long orderId = orderProduct.orderId().id();
        String productName = orderProduct.product().productName().productName();
        orderProductDao.insert(orderProduct);
        orderProductDao.deleteById(orderId, productName);
        OrderProduct foundOrderProduct = orderProductDao.findById(orderId, productName);
        assertNull(foundOrderProduct);
    }

    @Test
    public void testFindById() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 7, 1);
        //Date sqlDate = Date.valueOf(localDate);

        Order order = new Order(
                new Id(1L),
                null,
                Status.NEW,
                new CreateDateTime(localDate.atStartOfDay()),
                new UpdateDateTime(localDate.atStartOfDay()),
                new ArrayList<>()
        );

        Product product = new Product(
                new Id(1L), null, null, null, null, null, null, null, null, null, null, null
        );

        OrderProduct orderProduct = new OrderProduct(
                new Id(1L),
                order,
                product,
                new Quantity(3),
                new Price(323),
                new Id(null)
        );
        Long orderId = orderProduct.orderId().id();
        String productName = orderProduct.product().productName().productName();
        orderProductDao.insert(orderProduct);
        OrderProduct foundOrderProduct = orderProductDao.findById(orderId, productName);
        assertNotNull(foundOrderProduct);

        orderProductDao.deleteById(orderId, productName);
    }
}
