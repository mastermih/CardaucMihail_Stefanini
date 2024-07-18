package DaoValueTest;

import com.ImperioElevator.ordermanagement.OrderManagementApplication;
import com.ImperioElevator.ordermanagement.dao.daoimpl.CategoryDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Category;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = OrderManagementApplication.class)
@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class CategoryDaoImplTest {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDaoImplTest.class);

    @Autowired
    private CategoryDaoImpl categoryDao;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Database URL: {}", connection.getMetaData().getURL());
            logger.info("Database User: {}", connection.getMetaData().getUserName());
        }
    }

    @Test
    public void testInsert() throws SQLException {
        Category category = new Category(
                null,
                new Name("HzBrat"),
                null
        );
        Long generatedId = categoryDao.insert(category);
        Category foundCategory = categoryDao.findById(new Id(generatedId).getId());
        assertNotNull(foundCategory);
        assertEquals(generatedId, foundCategory.getId().getId());
        //categoryDao.deleteById(new Id(generatedId).getId());
    }

    @Test
    public void testInsertWithParent() throws SQLException {
        Category parentCategory = new Category(
                null,
                new Name("Lucreza"),
                null
        );
        Long generatedIdParent = categoryDao.insert(parentCategory);
        parentCategory.setId(new Id(generatedIdParent));

        Category category = new Category(
                null,
                new Name("Da Lucreaza"),
                parentCategory
        );
        Long generatedIdChild = categoryDao.insert(category);
        category.setId(new Id(generatedIdChild));

        Category foundCategory = categoryDao.findById(new Id(generatedIdChild).getId());
        assertNotNull(foundCategory);
        assertNotNull(foundCategory.getParentId());
        assertEquals(parentCategory.getId().getId(), foundCategory.getParentId().getId().getId());

        categoryDao.deleteById(new Id(generatedIdChild).getId());
        categoryDao.deleteById(new Id(generatedIdParent).getId());
    }

    @Test
    public void testDelete() throws SQLException {
        Category category = new Category(
                null,
                new Name("EHUUUU"),
                null
        );
        Long generatedId = categoryDao.insert(category);
        categoryDao.deleteById(new Id(generatedId).getId());
        Category foundCategory = categoryDao.findById(new Id(generatedId).getId());
        assertNull(foundCategory);
    }

    @Test
    public void testFindById() throws SQLException {
        Category category = new Category(
                null,
                new Name("Pabedaaaaa"),
                null
        );
        Long generatedId = categoryDao.insert(category);
        category.setId(new Id(generatedId));
        Category foundCategory = categoryDao.findById(new Id(generatedId).getId());
        assertNotNull(foundCategory);
        assertEquals(category.getId().getId(), foundCategory.getId().getId());
        assertEquals(category.getName().getName(), foundCategory.getName().getName());
        categoryDao.deleteById(new Id(generatedId).getId());
    }

    @Test
    public void testUpdate() throws SQLException {
        Category category = new Category(
                null,
                new Name("GoGoGOGO"),
                null
        );
        Long generatedId = categoryDao.insert(category);
        category.setId(new Id(generatedId));
        category.setName(new Name("NONONONONO"));
        categoryDao.update(category);
        Category foundCategory = categoryDao.findById(new Id(generatedId).getId());
        assertNotNull(foundCategory);
        assertEquals("NONONONONO", foundCategory.getName().getName());
        categoryDao.deleteById(new Id(generatedId).getId());
    }
}
