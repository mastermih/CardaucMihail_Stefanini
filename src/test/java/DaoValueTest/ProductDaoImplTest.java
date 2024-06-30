package DaoValueTest;

import DaoImplementation.ProductDaoImpl;
import Entity.Product;
import JDBC.DatabaseConnectionDemo;
import ValueObjects.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoImplTest {
    private static Connection connection;
    private static ProductDaoImpl productDao;

    @BeforeAll
    public static void setUp() {
        connection = DatabaseConnectionDemo.open();
        productDao = new ProductDaoImpl(connection);
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }



    @Test
    public void testInsert() throws SQLException {
        Product product = new Product(
                new ID(29),
                new Price(Integer.valueOf(100)),
                new Width(Double.valueOf(50.00)),
                new Height(Double.valueOf(20.00)),
                new Depth(Double.valueOf(30.00)),
                new Category(1, null, null),
                //new Category(1, "Category", null),
                new ProductBrand("Brand"),
                new ProductName("Name"),
                new ElectricityConsumption(10),
                new Description("Description")
        );
        productDao.insert(product);

        Product foundProduct = productDao.findById(29L);
        assertNotNull(foundProduct);
        assertEquals(product.getProduct_id().getId(), foundProduct.getProduct_id().getId());
        assertEquals(product.getProductName().getProductName(), foundProduct.getProductName().getProductName());
    }

    @Test
    public void testUpdate() throws SQLException {
        Product product = new Product(
                new ID(30),
                new Price(Integer.valueOf(200)),
                new Width(Double.valueOf(60.00)),
                new Height(Double.valueOf(25.00)),
                new Depth(Double.valueOf(35.00)),
                new Category(2, null, null),
                new ProductBrand("Brand"),
                new ProductName("Name"),
                new ElectricityConsumption(20),
                new Description("Description")
        );
        productDao.insert(product);

        product.setProductName(new ProductName("Updated Name"));
        productDao.update(product);

        Product foundProduct = productDao.findById(30L);
        assertNotNull(foundProduct);
        assertEquals("Updated Name", foundProduct.getProductName().getProductName());

    }

    @Test
    public void testDeleteById() throws SQLException {
        Product product = new Product(
                new ID(31),
                new Price(Integer.valueOf(300)),
                new Width(Double.valueOf(70.00)),
                new Height(Double.valueOf(30.00)),
                new Depth(Double.valueOf(40.00)),
                new Category(3, null, null),
                new ProductBrand("Brand"),
                new ProductName("Name"),
                new ElectricityConsumption(30),
                new Description("Description")
        );
        productDao.insert(product);

        productDao.deleteById(31L);
        Product foundProduct = productDao.findById(31L);
        assertNull(foundProduct);
    }

    @Test
    public void testFindById() throws SQLException {
        Product product = new Product(
                new ID(32),
                new Price(Integer.valueOf(400)),
                new Width(Double.valueOf(80.00)),
                new Height(Double.valueOf(35.00)),
                new Depth(Double.valueOf(45.00)),
                new Category(4, null, null),
                new ProductBrand("Brand"),
                new ProductName("Name"),
                new ElectricityConsumption(40),
                new Description("Description")
        );
        productDao.insert(product);

        Product foundProduct = productDao.findById(32L);
        assertNotNull(foundProduct);
        assertEquals(product.getProduct_id().getId(), foundProduct.getProduct_id().getId());
        assertEquals(product.getProductName().getProductName(), foundProduct.getProductName().getProductName());
    }

    @Test
    public void testGetProductPagination() throws SQLException {
        for (int i = 5; i <= 10; i++) {
            Product product = new Product(
                    new ID(i),
                    new Price(Integer.valueOf(100 + i)),
                    new Width(Double.valueOf(50.00 + i)),
                    new Height(Double.valueOf(20.00 + i)),
                    new Depth(Double.valueOf(30.00 + i)),
                    new Category(i, null, null),
                    new ProductBrand("Brand"),
                    new ProductName("Name" + i),
                    new ElectricityConsumption(10 + i),
                    new Description("Description" + i)
            );
            productDao.insert(product);
        }

        List<Product> products = productDao.getProductPagination(3);
        assertEquals(3, products.size());
    }
}