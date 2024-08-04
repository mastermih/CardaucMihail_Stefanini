package DaoValueTest;

import com.ImperioElevator.ordermanagement.OrderManagementApplication;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.entity.Category;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.CategoryType;
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
                new Description("Description"),
                new Image(""),
                CategoryType.Elevator

                );
        Long generatedId = productDao.insert(product);
        Product foundProduct = productDao.findById(new Id(generatedId).id());
        assertNotNull(foundProduct);
        assertEquals(generatedId, foundProduct.productId().id());
        assertEquals(product.productName().getProductName(), foundProduct.productName().getProductName());
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
                new Description("Description"),
                new Image(""),
                CategoryType.Elevator


        );
        Long generatedId = productDao.insert(product);
        product = new Product(new Id(generatedId), new Price(200), product.width(), product.height(), product.depth(), product.category(), product.productBrand(), product.productName(), product.electricityConsumption(), product.description(), product.path(), product.categoryType());
        productDao.update(product);

        Product foundProduct = productDao.findById(new Id(generatedId).id());
        assertNotNull(foundProduct);
        assertEquals(200.0, foundProduct.price().getPrice());
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
                new Description("Description"),
                new Image(""),
                CategoryType.Elevator

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
                new Description("Description"),
                new Image(""),
                CategoryType.Elevator


        );
        Long generatedId = productDao.insert(product);
        product = new Product(new Id(generatedId), product.price(), product.width(), product.height(), product.depth(), product.category(), product.productBrand(), product.productName(), product.electricityConsumption(), product.description(), product.path(), product.categoryType());
        Product foundProduct = productDao.findById(generatedId);
        assertNotNull(foundProduct);
        assertEquals(product.productId().id(), foundProduct.productId().id());
        assertEquals(product.productName().getProductName(), foundProduct.productName().getProductName());
        productDao.deleteById(generatedId);
    }

}