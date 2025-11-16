package ru.ssau.tk.labwork.ooplabworks.service;

import org.junit.jupiter.api.*;
import ru.ssau.tk.labwork.ooplabworks.config.DataSourceConfig;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcUsersDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcFunctionsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcPointsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.UsersDao;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class SearchServiceSortTest {

    private UsersDao usersDao;
    private SearchService service;

    @BeforeEach
    void setup() throws Exception {

        DataSource ds = DataSourceConfig.create(
                "jdbc:postgresql://localhost:5432/lab",
                "postgres",
                "123"
        );

        usersDao = new JdbcUsersDao(ds);
        service = new SearchService(
                usersDao,
                new JdbcFunctionsDao(ds),
                new JdbcPointsDao(ds)
        );

        try (Connection c = ds.getConnection();
             Statement st = c.createStatement()) {
            st.execute("DELETE FROM users");
        }

        usersDao.insert(new UserDTO("boris", "CIVIL", "x", true));
        usersDao.insert(new UserDTO("alex", "CIVIL", "x", true));
        usersDao.insert(new UserDTO("dima", "CIVIL", "x", true));
    }

    @Test
    void testSortUsersByLogin() {

        List<UserDTO> sorted = service.findUsersSortedByLogin();

        Assertions.assertEquals("alex", sorted.get(0).getLogin());
        Assertions.assertEquals("boris", sorted.get(1).getLogin());
        Assertions.assertEquals("dima", sorted.get(2).getLogin());
    }
}
