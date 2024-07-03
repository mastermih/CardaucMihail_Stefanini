package DaoValueTest;

import daoimplementation.OrderDaoImpl;
import daoimplementation.OrderProductDaoImpl;
import entity.Order;
import entity.OrderProduct;
import entity.Product;
import jdbc.DatabaseConnectionDemo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import valueobjects.Id;
import valueobjects.Price;
import valueobjects.Quantity;


import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

//Ele lucreaza dar depind de Order asa ca de asta pot cadea
//Problema nu este rezolvata inca
public class OrderProductDaoImplTest {
    private static Connection connection;
    private static OrderProductDaoImpl orderProductDao;

    @BeforeAll
    public static void setUp() {
        connection = DatabaseConnectionDemo.open();
        orderProductDao = new OrderProductDaoImpl(connection);
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    @Test
    public void testInser() throws SQLException {
        OrderProduct orderProduct = new OrderProduct(
                new Order(new Id(1), null, null, null, null),
                new Product(new Id(1), null, null, null,null,null,null,null, null, null),
                new Quantity(1),
                new Price(323)
        );
        long orderId = orderProduct.getOrderId().getOrderId().getId();
        long productId = orderProduct.getProductId().getProductId().getId();
        orderProductDao.insert(orderProduct);
        assertNotNull(orderProduct);
        orderProductDao.deleteById(orderId, productId);
    }

    @Test
    public void testUpdate() throws SQLException {
        OrderProduct orderProduct = new OrderProduct(
                new Order(new Id(1), null, null, null, null),
                new Product(new Id(1), null, null, null,null,null,null,null, null, null),
                new Quantity(1),
                new Price(323)
        );

        long orderId = orderProduct.getOrderId().getOrderId().getId();
        long productId = orderProduct.getProductId().getProductId().getId();
        orderProductDao.insert(orderProduct);
        orderProduct.setQuantity(new Quantity(2));
        orderProduct.setPriceOrder(new Price(500));

        orderProductDao.update(orderProduct);
        assertEquals(2, orderProduct.getQuantity().getQuantity());
        assertEquals(500, orderProduct.getPriceOrder().getPrice(), orderProduct.getPriceOrder().getPrice());
        //asertinurile lipsesc
        orderProductDao.deleteById(orderId, productId);
    }

    @Test
    public void testDelete() throws SQLException {
        OrderProduct orderProduct = new OrderProduct(
                new Order(new Id(1), null, null, null, null),
                new Product(new Id(1), null, null, null,null,null,null,null, null, null),
                new Quantity(1),
                new Price(323)
        );

        long orderId = orderProduct.getOrderId().getOrderId().getId();
        long productId = orderProduct.getProductId().getProductId().getId();
        orderProductDao.insert(orderProduct);
        orderProductDao.deleteById(orderId, productId);
        OrderProduct orderProductFiend = orderProductDao.findById(orderId);
        assertNull(orderProductFiend);

    }

    @Test
    public void testFindById() throws SQLException {
        OrderProduct orderProduct = new OrderProduct(
                new Order(new Id(1), null, null, null, null),
                new Product(new Id(1), null, null, null,null,null,null,null, null, null),
                new Quantity(1),
                new Price(323)
        );

        long orderId = orderProduct.getOrderId().getOrderId().getId();
        long productId = orderProduct.getProductId().getProductId().getId();
        orderProductDao.insert(orderProduct);
        OrderProduct foundOrderProduct = orderProductDao.findById(orderId, productId);
        assertNotNull(foundOrderProduct);

        //asertinurile lipsesc
        orderProductDao.deleteById(orderId, productId);
    }

}
