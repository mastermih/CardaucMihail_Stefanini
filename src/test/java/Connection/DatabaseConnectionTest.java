package Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseConnectionTest {

    private Connection connection;
    private Properties properties = new Properties();

    @Before
    public void setUp() throws SQLException, IOException {
        properties.load(new FileInputStream("src/main/resources/config.properties"));

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.name");
        String password = properties.getProperty("db.password");

        connection = DriverManager.getConnection(url, username, password);
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testConnection() {
        assertNotNull(connection);
        try {
            assertTrue(connection.isValid(5));
        } catch (SQLException e) {
            fail("Connection validation failed: " + e.getMessage());
        }
    }
}
