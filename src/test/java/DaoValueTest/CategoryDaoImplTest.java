package DaoValueTest;

import DaoImplementation.CategoryDaoImpl;
import DaoImplementation.OrderDaoImpl;
import DaoImplementation.ProductDaoImpl;
import Entity.Category;
import Entity.Product;
import JDBC.DatabaseConnectionDemo;
import ValueObjects.*;
import org.junit.jupiter.api.*;

import javax.lang.model.element.Name;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDaoImplTest {
    private static Connection connection;
    private static CategoryDaoImpl categoryDao;

    @BeforeAll
    public static void setUp() {
        connection = DatabaseConnectionDemo.open();
        categoryDao = new CategoryDaoImpl(connection);
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    @Test
    public void testInsert() throws SQLException
    {
        Category category = new Category(15L, "TestCat", null);
        categoryDao.insert(category);
        Category foundCategory = categoryDao.findById(15L);
        assertNotNull(foundCategory);
        assertEquals(15L, foundCategory.getId());
        categoryDao.deleteById(15L);
    }
    @Test
    public void testDelete() throws SQLException {
        Category category = new Category(14L, "TestCat2", null);
        categoryDao.insert(category);
        categoryDao.deleteById(14L);
    }
    @Test
    public void testFiendById()throws SQLException
    {
        Category category = new Category(16L, "TestCat3", null);
        categoryDao.insert(category);
        Category newCategory  = categoryDao.findById(16L);
        assertNotNull(category);
        assertEquals(category.getId(), newCategory.getId());
        assertEquals(category.getName(), newCategory.getName());
        categoryDao.deleteById(16L);
    }
    @Test
    public void testUpdate() throws SQLException
    {
        Category category = new Category(21L, "TestCat4", null);
        categoryDao.insert(category);
        category.setName("HESOYAM");
        categoryDao.update(category);
        Category fiendCategory = categoryDao.findById(21L);
        assertNotNull(fiendCategory);
        assertEquals("HESOYAM", fiendCategory.getName());
        categoryDao.deleteById(21L);
    }
}