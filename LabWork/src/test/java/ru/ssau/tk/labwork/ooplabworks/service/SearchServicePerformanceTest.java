package ru.ssau.tk.labwork.ooplabworks.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.config.DataSourceConfig;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcUsersDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcFunctionsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcPointsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.UsersDao;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;

import javax.sql.DataSource;
import java.util.Random;

public class SearchServicePerformanceTest {

    private SearchService service;
    private UsersDao usersDao;

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
    }

    @Test
    void prepareUsers() throws Exception {
        // Генерируем 10к случайных логинов
        for (int i = 0; i < 10000; i++) {
            String login = "user" + i + "_" + new Random().nextInt(1000000);
            usersDao.insert(new UserDTO(login, "CIVIL", "pwd", true));
        }
        System.out.println("Inserted 10k users.");
    }

    @Test
    void testSortUsersPerformance() {
        long start = System.currentTimeMillis();

        service.findUsersSortedByLogin();

        long end = System.currentTimeMillis();
        System.out.println("SORT 10k Users = " + (end - start) + " ms");
    }
}
