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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
//ToDO Pina luni 23 Sept vor fi testele gata
@SpringBootTest(classes = OrderManagementApplication.class)
@ActiveProfiles("test")

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
        Category foundCategory = categoryDao.findById(new Id(generatedId).id());
        assertNotNull(foundCategory);
        assertEquals(generatedId, foundCategory.id().id());
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
        parentCategory = new Category(new Id(generatedIdParent), parentCategory.name(), null);

        Category category = new Category(
                null,
                new Name("Da Lucreaza"),
                parentCategory
        );
        Long generatedIdChild = categoryDao.insert(category);
        category = new Category(new Id(generatedIdChild), category.name(), parentCategory);

        Category foundCategory = categoryDao.findById(new Id(generatedIdChild).id());
        assertNotNull(foundCategory);
        assertNotNull(foundCategory.parentId());
        assertEquals(parentCategory.id().id(), foundCategory.parentId().id().id());

        categoryDao.deleteById(new Id(generatedIdChild).id());
        categoryDao.deleteById(new Id(generatedIdParent).id());
    }

    @Test
    public void testDelete() throws SQLException {
        Category category = new Category(
                null,
                new Name("EHUUUU"),
                null
        );
        Long generatedId = categoryDao.insert(category);
        categoryDao.deleteById(new Id(generatedId).id());
        Category foundCategory = categoryDao.findById(new Id(generatedId).id());
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
        category = new Category(new Id(generatedId), category.name(), null);
        Category foundCategory = categoryDao.findById(new Id(generatedId).id());
        assertNotNull(foundCategory);
        assertEquals(category.id().id(), foundCategory.id().id());
        assertEquals(category.name().name(), foundCategory.name().name());
        categoryDao.deleteById(new Id(generatedId).id());
    }

    @Test
    public void testUpdate() throws SQLException {
        Category category = new Category(
                null,
                new Name("GoGoGOGO"),
                null
        );
        Long generatedId = categoryDao.insert(category);
        category = new Category(new Id(generatedId), new Name("NONONONONO"), null);
        category.name().name();
        categoryDao.update(category);
        Category foundCategory = categoryDao.findById(new Id(generatedId).id());
        assertNotNull(foundCategory);
        assertEquals("NONONONONO", foundCategory.name().name());
        categoryDao.deleteById(new Id(generatedId).id());
    }
//Trebu de sters
    @Test
    public void testSterger() {
        System.out.println(checkPassword("tolik", "$2a$09$z0CEV1.OLM/vgJcR8VGageQCHtMEHQjjZy1zYnTX4t0JNXcP.VJF"));  // Should return true
      //  System.out.println(checkPassword("tolik", "$2a$09$7S1jzVsjxu5JCJFHwlvO2OgLXpFlZHncSb6f0cWf9i4GXHFpjgRxm"));  // Should return true

    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(9);
        return encoder.matches(rawPassword, encodedPassword);
    }


}
