package ru.ssau.tk.labwork.ooplabworks.dao.performance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.config.DataSourceConfig;
import ru.ssau.tk.labwork.ooplabworks.dao.FunctionsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.PointsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.UsersDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcPointsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcFunctionsDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcUsersDao;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;
import ru.ssau.tk.labwork.ooplabworks.dto.PointDTO;

import javax.sql.DataSource;

public class ManualPointsPerformanceTest {

    private PointsDao pointsDao;
    private FunctionsDao functionsDao;
    private UsersDao usersDao;

    @BeforeEach
    void setup() throws Exception {
        DataSource ds = DataSourceConfig.create(
                "jdbc:postgresql://localhost:5432/lab",
                "postgres",
                "123"
        );

        pointsDao = new JdbcPointsDao(ds);
        functionsDao = new JdbcFunctionsDao(ds);
        usersDao = new JdbcUsersDao(ds);

        // создаём пользователя и функцию
        usersDao.insert(new UserDTO(null, "owner_" + System.currentTimeMillis(), "CIVIL", "pass", true));
        functionsDao.insert(new FunctionDTO(null, 1, "basefunc", "sig"));
    }

    @Test
    void testInsert10kPoints() throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            pointsDao.insert(new PointDTO(null, 1, i, i * 2.0));
        }

        long end = System.currentTimeMillis();
        System.out.println("POINTS INSERT 10k = " + (end - start) + " ms");
    }

    @Test
    void testSelect10kPoints() throws Exception {
        for (int i = 0; i < 10000; i++) {
            pointsDao.insert(new PointDTO(null, 1, i, i * 2.0));
        }

        long start = System.currentTimeMillis();

        for (int i = 1; i <= 10000; i++) {
            pointsDao.findById(i);
        }

        long end = System.currentTimeMillis();
        System.out.println("POINTS SELECT 10k = " + (end - start) + " ms");
    }

    @Test
    void testUpdate10kPointsManual() throws Exception {

        // Создаём 10k точек
        for (int i = 0; i < 10000; i++) {
            pointsDao.insert(new PointDTO(null, 1, i * 1.0, i * 2.0));
        }

        long start = System.currentTimeMillis();

        // Обновляем 10k точек
        for (int id = 1; id <= 10000; id++) {
            pointsDao.update(id, id * 10.0, id * 20.0);
        }

        long end = System.currentTimeMillis();

        System.out.println("POINTS UPDATE 10k = " + (end - start) + " ms");
    }

    @Test
    void testDelete10kPoints() throws Exception {
        for (int i = 0; i < 10000; i++) {
            pointsDao.insert(new PointDTO(null, 1, i, i * 2.0));
        }

        long start = System.currentTimeMillis();

        for (int i = 1; i <= 10000; i++) {
            pointsDao.delete(i);
        }

        long end = System.currentTimeMillis();
        System.out.println("POINTS DELETE 10k = " + (end - start) + " ms");
    }
}
