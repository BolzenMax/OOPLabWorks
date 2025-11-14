package ru.ssau.tk.labwork.ooplabworks.dao;


import com.github.javafaker.Faker;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.ssau.tk.labwork.ooplabworks.config.DataSourceConfig;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcFunctionsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcUsersDao;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FunctionsDaoTest {

    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("lab")
                    .withUsername("postgres")
                    .withPassword("postgres");

    private DataSource ds;
    private UsersDao usersDao;
    private FunctionsDao functionsDao;
    private final Faker faker = new Faker();

    @BeforeAll
    void setup() throws Exception {
        postgres.start();

        ds = DataSourceConfig.create(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        String schema = new String(Files.readAllBytes(Paths.get("src/main/resources/schema.sql")));
        try (Connection c = ds.getConnection(); Statement st = c.createStatement()) {
            st.execute(schema);
        }

        usersDao = new JdbcUsersDao(ds);
        functionsDao = new JdbcFunctionsDao(ds);
    }

    @Test
    void testCRUD() {
        // create user
        UserDTO user = new UserDTO(null, faker.name().username(), "CIVIL", "pass123", true);
        int userId = usersDao.insert(user);

        // CREATE function
        FunctionDTO f = new FunctionDTO(null, userId, "testFunc", "x+1");
        int funcId = functionsDao.insert(f);
        assertTrue(funcId > 0);

        // READ
        FunctionDTO fromDb = functionsDao.findById(funcId).orElseThrow();
        assertEquals("testFunc", fromDb.getName());

        // UPDATE
        functionsDao.updateName(funcId, "newFunc");
        functionsDao.updateSignature(funcId, "x*2");

        FunctionDTO updated = functionsDao.findById(funcId).orElseThrow();
        assertEquals("newFunc", updated.getName());
        assertEquals("x*2", updated.getSignature());

        // DELETE
        functionsDao.delete(funcId);
        assertTrue(functionsDao.findById(funcId).isEmpty());
    }
}