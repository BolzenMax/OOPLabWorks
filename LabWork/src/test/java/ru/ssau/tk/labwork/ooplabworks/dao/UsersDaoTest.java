package ru.ssau.tk.labwork.ooplabworks.dao;


import com.github.javafaker.Faker;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.ssau.tk.labwork.ooplabworks.config.DataSourceConfig;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcUsersDao;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsersDaoTest {

    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("lab")
                    .withUsername("postgres")
                    .withPassword("postgres");

    private DataSource ds;
    private UsersDao dao;
    private final Faker faker = new Faker();

    @BeforeAll
    void setup() throws Exception {
        postgres.start();

        ds = DataSourceConfig.create(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        // schema.sql
        String schema = new String(Files.readAllBytes(Paths.get("src/main/resources/scripts/schema.sql")));
        try (Connection c = ds.getConnection(); Statement st = c.createStatement()) {
            st.execute(schema);
        }

        dao = new JdbcUsersDao(ds);
    }

    @Test
    void testCRUD() {
        String login = faker.name().username();

        // CREATE
        UserDTO user = new UserDTO(null, login, "CIVIL", "12345", true);
        int id = dao.insert(user);
        assertTrue(id > 0);

        // READ
        UserDTO fromDb = dao.findById(id).orElse(null);
        assertNotNull(fromDb);
        assertEquals(login, fromDb.getLogin());

        // UPDATE
        dao.updateRole(id, "ADMIN");
        dao.updatePassword(id, "newpass");
        dao.updateEnabled(id, false);

        UserDTO updated = dao.findById(id).orElseThrow();
        assertEquals("ADMIN", updated.getRole());
        assertEquals("newpass", updated.getPassword());
        assertFalse(updated.isEnabled());

        // DELETE
        dao.delete(id);
        assertTrue(dao.findById(id).isEmpty());
    }
}
