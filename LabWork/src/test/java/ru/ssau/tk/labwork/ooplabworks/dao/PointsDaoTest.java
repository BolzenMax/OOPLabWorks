package ru.ssau.tk.labwork.ooplabworks.dao;

import com.github.javafaker.Faker;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.ssau.tk.labwork.ooplabworks.config.DataSourceConfig;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcFunctionsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcPointsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcUsersDao;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;
import ru.ssau.tk.labwork.ooplabworks.dto.PointDTO;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PointsDaoTest {

    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");

    private DataSource ds;
    private UsersDao usersDao;
    private FunctionsDao functionsDao;
    private PointsDao pointsDao;

    @BeforeAll
    void setup() throws Exception {
        postgres.start();

        ds = DataSourceConfig.create(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        String schema = new String(Files.readAllBytes(Paths.get("src/main/resources/scripts/schema.sql")));
        try (Connection c = ds.getConnection(); Statement st = c.createStatement()) {
            st.execute(schema);
        }

        usersDao = new JdbcUsersDao(ds);
        functionsDao = new JdbcFunctionsDao(ds);
        pointsDao = new JdbcPointsDao(ds);
    }

    @Test
    void testCRUD() {
        UserDTO user = new UserDTO(null, "testUser", "CIVIL", "123", true);
        int userId = usersDao.insert(user);

        FunctionDTO f = new FunctionDTO(null, userId, "myFunc", "x^2");
        int funcId = functionsDao.insert(f);

        // CREATE
        PointDTO p = new PointDTO(null, funcId, 2.0, 4.0);
        int pointId = pointsDao.insert(p);

        assertTrue(pointId > 0);

        // READ
        PointDTO fromDb = pointsDao.findById(pointId).orElseThrow();
        assertEquals(2.0, fromDb.getX(), 1e-9);

        // UPDATE
        pointsDao.update(pointId, 3.0, 9.0);
        PointDTO updated = pointsDao.findById(pointId).orElseThrow();
        assertEquals(9.0, updated.getY(), 1e-9);

        // DELETE
        pointsDao.delete(pointId);
        assertTrue(pointsDao.findById(pointId).isEmpty());
    }
}