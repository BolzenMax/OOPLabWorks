package ru.ssau.tk.labwork.ooplabworks.dao.performance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.config.DataSourceConfig;
import ru.ssau.tk.labwork.ooplabworks.dao.FunctionsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.UsersDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcFunctionsDao;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcUsersDao;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;

import javax.sql.DataSource;

public class ManualFunctionsPerformanceTest {

    private FunctionsDao functionsDao;
    private UsersDao usersDao;

    @BeforeEach
    void setup() throws Exception {
        DataSource ds = DataSourceConfig.create(
                "jdbc:postgresql://localhost:5432/lab",
                "postgres",
                "123"
        );

        functionsDao = new JdbcFunctionsDao(ds);
        usersDao = new JdbcUsersDao(ds);

        // Создаем 1 пользователя, чтобы не нарушать внешний ключ
        usersDao.insert(new UserDTO(null, "owner_" + System.currentTimeMillis(), "CIVIL", "pass", true));
    }

    @Test
    void testInsert10kFunctions() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            functionsDao.insert(new FunctionDTO(null, 1, "func" + i, "signature" + i));
        }

        long end = System.currentTimeMillis();
        System.out.println("FUNCTIONS INSERT 10k = " + (end - start) + " ms");
    }

    @Test
    void testSelect10kFunctions() throws Exception {
        for (int i = 0; i < 10000; i++) {
            functionsDao.insert(new FunctionDTO(null, 1, "func" + i, "signature" + i));
        }

        long start = System.currentTimeMillis();

        for (int i = 1; i <= 10000; i++) {
            functionsDao.findById(i);
        }

        long end = System.currentTimeMillis();
        System.out.println("FUNCTIONS SELECT 10k = " + (end - start) + " ms");
    }

    @Test
    void testUpdate10kFunctions() throws Exception {
        for (int i = 0; i < 10000; i++) {
            functionsDao.insert(new FunctionDTO(null, 1, "func" + i, "signature" + i));
        }

        long start = System.currentTimeMillis();

        for (int i = 1; i <= 10000; i++) {
            functionsDao.updateName(i, "new_name_" + i);
        }

        long end = System.currentTimeMillis();
        System.out.println("FUNCTIONS UPDATE 10k = " + (end - start) + " ms");
    }

    @Test
    void testDelete10kFunctions() throws Exception {
        for (int i = 0; i < 10000; i++) {
            functionsDao.insert(new FunctionDTO(null, 1, "func" + i, "signature" + i));
        }

        long start = System.currentTimeMillis();

        for (int i = 1; i <= 10000; i++) {
            functionsDao.delete(i);
        }

        long end = System.currentTimeMillis();
        System.out.println("FUNCTIONS DELETE 10k = " + (end - start) + " ms");
    }
}
