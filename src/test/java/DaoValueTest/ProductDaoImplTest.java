package DaoValueTest;

import daoimplementation.ProductDaoImpl;
import entity.Category;
import entity.Product;
import jdbc.DatabaseConnectionDemo;
import valueobjects.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
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
                null,
                new Price(Integer.valueOf(100)),
                new Width(Double.valueOf(50.00)),
                new Height(Double.valueOf(20.00)),
                new Depth(Double.valueOf(30.00)),
                new Category(new Id(1), null, null),
                new ProductBrand("Brand"),
                new ProductName("Name"),
                new ElectricityConsumption(10),
                new Description("Description")
        );
                long generatedId = productDao.insert(product);
                 //product.setProductId(new Id(generatedId));
        Product foundProduct = productDao.findById(generatedId);
        assertNotNull(foundProduct);
        assertEquals(generatedId, foundProduct.getProductId().getId());
        assertEquals(product.getProductName().getProductName(), foundProduct.getProductName().getProductName());
        productDao.deleteById(generatedId);
    }

    @Test
    public void testUpdate() throws SQLException {
        Product product = new Product(
                null,
                new Price(Integer.valueOf(200)),
                new Width(Double.valueOf(60.00)),
                new Height(Double.valueOf(25.00)),
                new Depth(Double.valueOf(35.00)),
                new Category(new Id(1), null, null),
                new ProductBrand("Brand"),
                new ProductName("Name"),
                new ElectricityConsumption(20),
                new Description("Description")
        );
                long generatedId = productDao.insert(product);
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
                new Price(Integer.valueOf(300)),
                new Width(Double.valueOf(70.00)),
                new Height(Double.valueOf(30.00)),
                new Depth(Double.valueOf(40.00)),
                new Category(new Id(1), null, null),
                new ProductBrand("Brand"),
                new ProductName("Name"),
                new ElectricityConsumption(30),
                new Description("Description")
        );
        long generatedId = productDao.insert(product);

        productDao.deleteById(generatedId);
        Product foundProduct = productDao.findById(generatedId);
        assertNull(foundProduct);
        productDao.deleteById(generatedId);
    }

    @Test
    public void testFindById() throws SQLException {
        Product product = new Product(
                null,
                new Price(Integer.valueOf(400)),
                new Width(Double.valueOf(80.00)),
                new Height(Double.valueOf(35.00)),
                new Depth(Double.valueOf(45.00)),
                new Category(new Id(1), null, null),
                new ProductBrand("Brand"),
                new ProductName("Name"),
                new ElectricityConsumption(40),
                new Description("Description")
        );
        long generatedId = productDao.insert(product);
        product.setProductId(new Id(generatedId));

        Product foundProduct = productDao.findById(generatedId);
        assertNotNull(foundProduct);
        assertEquals(product.getProductId().getId(), foundProduct.getProductId().getId());
        assertEquals(product.getProductName().getProductName(), foundProduct.getProductName().getProductName());
         productDao.deleteById(generatedId);
    }

    @Test
    public void testGetProductPagination() throws SQLException {
        for (int i = 37; i <= 47; i++) {
            Product product = new Product(
                    null,
                    new Price(Integer.valueOf(100 + i)),
                    new Width(Double.valueOf(50.00 + i)),
                    new Height(Double.valueOf(20.00 + i)),
                    new Depth(Double.valueOf(30.00 + i)),
                    new Category(new Id(i), null, null),
                    new ProductBrand("Brand"),
                    new ProductName("Name" + i),
                    new ElectricityConsumption(10 + i),
                    new Description("Description" + i)
            );
            long generatedId = productDao.insert(product);
        }

        List<Product> products = productDao.getProductPagination(3);
        assertEquals(3, products.size());
        for (int i = 37; i <= 47; i++) {
            productDao.deleteById(i);
        }
    }
}