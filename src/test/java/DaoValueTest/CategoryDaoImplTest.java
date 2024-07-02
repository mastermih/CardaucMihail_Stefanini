package DaoValueTest;

import daoimplementation.CategoryDaoImpl;
import entity.Category;
import jdbc.DatabaseConnectionDemo;
import org.junit.jupiter.api.*;
import valueobjects.Id;
import valueobjects.Name;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDaoImplTest  {
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
    //No parent_id
    @Test
    public void testInsert() throws SQLException
    {
        Category category = new Category(
           null,
           new Name("HzBrat"),
           null
        );
        long generatedId = categoryDao.insert(category);
        Category foundCategory = categoryDao.findById(generatedId);
        assertNotNull(foundCategory);
        assertEquals(generatedId, foundCategory.getId().getId());
        categoryDao.deleteById(generatedId);
    }
    @Test
    public void testInsertWithParent() throws SQLException {
        Category parentCategory = new Category(
                null,
                new Name("Lucreza"),
                null
        );
        long generatedIdParent = categoryDao.insert(parentCategory);
        parentCategory.setId(new Id(generatedIdParent));

        Category category = new Category(
                null,
                new Name("Da Lucreaza"),
                parentCategory  // Correctly set the parent category
        );
        long generatedIdChild = categoryDao.insert(category);
        category.setId(new Id(generatedIdChild));

        Category foundCategory = categoryDao.findById(generatedIdChild);
        assertNotNull(foundCategory);
        assertNotNull(foundCategory.getParentId());
        assertEquals(parentCategory.getId().getId(), foundCategory.getParentId().getId().getId());

        // Clean up
        categoryDao.deleteById(generatedIdChild);
        categoryDao.deleteById(generatedIdParent);
    }

    @Test
    public void testDelete() throws SQLException {
        Category category = new Category(
                null,
                new Name("EHUUUU"),
                null
        );
        long generatedId = categoryDao.insert(category);
        categoryDao.deleteById(generatedId);
    }
    @Test
    public void testFiendById()throws SQLException
    {
        Category category = new Category(
                null,
                new Name("Pabedaaaaa"),
                null
        );
        long generatedId = categoryDao.insert(category);
        category.setId(new Id(generatedId));
        Category newCategory  = categoryDao.findById(generatedId);
        assertNotNull(category);
        assertEquals(category.getId().getId(), newCategory.getId().getId());
        assertEquals(category.getName().getName(), newCategory.getName().getName());
        categoryDao.deleteById(generatedId);
    }
    @Test
    public void testUpdate() throws SQLException
    {
        Category category = new Category(
                null,
                new Name("GoGoGOGO"),
                null
        );
        long generatedId = categoryDao.insert(category);
        category.setId(new Id(generatedId));
        category.setName(new Name("NONONONONO"));
        categoryDao.update(category);
        Category fiendCategory = categoryDao.findById(generatedId);
        assertNotNull(fiendCategory);
        assertEquals("NONONONONO", fiendCategory.getName().getName());
        categoryDao.deleteById(generatedId);
    }
}