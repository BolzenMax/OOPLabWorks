package ru.ssau.tk.labwork.ooplabworks.dao.performance;

import org.junit.jupiter.api.*;
import ru.ssau.tk.labwork.ooplabworks.config.DataSourceConfig;
import ru.ssau.tk.labwork.ooplabworks.dao.UsersDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcUsersDao;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.UUID;

public class ManualUsersPerformanceTest {

    private UsersDao dao;
    private DataSource dataSource;

    @BeforeEach
    void setup() throws Exception {

        dataSource = DataSourceConfig.create(
                "jdbc:postgresql://localhost:5432/lab",
                "postgres",
                "123"
        );

        // 🧹 ОЧИЩАЕМ ВСЮ ТАБЛИЦУ USERS перед началом каждого теста
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement()) {

            st.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");
        }

        dao = new JdbcUsersDao(dataSource);
    }

    @Test
    void testInsert10kManual() throws Exception {

        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {

            String login = "user_" + UUID.randomUUID();  // всегда уникальный логин!

            dao.insert(new UserDTO(null, login, "CIVIL", "pass", true));
        }

        long end = System.currentTimeMillis();
        System.out.println("MANUAL INSERT 10k = " + (end - start) + " ms");
    }


    @Test
    void testSelect10kManual() throws Exception {

        // генерируем 10k записей
        for (int i = 0; i < 10000; i++) {
            String login = "user_" + UUID.randomUUID();
            dao.insert(new UserDTO(null, login, "CIVIL", "pass", true));
        }

        long start = System.currentTimeMillis();

        for (int i = 1; i <= 10000; i++) {
            dao.findById(i);
        }

        long end = System.currentTimeMillis();
        System.out.println("MANUAL SELECT 10k = " + (end - start) + " ms");
    }


    @Test
    void testUpdate10kManual() throws Exception {

        for (int i = 0; i < 10000; i++) {
            String login = "user_" + UUID.randomUUID();
            dao.insert(new UserDTO(null, login, "CIVIL", "pass", true));
        }

        long start = System.currentTimeMillis();

        for (int i = 1; i <= 10000; i++) {
            dao.updatePassword(i, "newpass" + i);
        }

        long end = System.currentTimeMillis();
        System.out.println("MANUAL UPDATE 10k = " + (end - start) + " ms");
    }


    @Test
    void testDelete10kManual() throws Exception {

        for (int i = 0; i < 10000; i++) {
            String login = "user_" + UUID.randomUUID();
            dao.insert(new UserDTO(null, login, "CIVIL", "pass", true));
        }

        long start = System.currentTimeMillis();

        for (int i = 1; i <= 10000; i++) {
            dao.delete(i);
        }

        long end = System.currentTimeMillis();
        System.out.println("MANUAL DELETE 10k = " + (end - start) + " ms");
    }
}
