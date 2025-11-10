package ru.ssau.tk.labwork.ooplabworks.performances;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.ssau.tk.labwork.ooplabworks.entities.Function;
import ru.ssau.tk.labwork.ooplabworks.entities.Point;
import ru.ssau.tk.labwork.ooplabworks.entities.User;
import ru.ssau.tk.labwork.ooplabworks.repositories.FunctionRepository;
import ru.ssau.tk.labwork.ooplabworks.repositories.PointRepository;
import ru.ssau.tk.labwork.ooplabworks.repositories.UserRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PerformanceTest {

    private static final int RECORDS_COUNT = 10000;
    private static final String CSV_FILE = "PerformanceFramework.csv";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private PointRepository pointRepository;

    private final Random random = new Random();
    private List<User> createdUsers = new ArrayList<>();
    private List<Function> createdFunctions = new ArrayList<>();
    private List<Point> createdPoints = new ArrayList<>();

    // таблица

    @BeforeAll
    static void setupCsv() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            writer.write("Операция,Время (мс)");
            writer.newLine();
        }
    }

    private void appendResult(String operation, long duration) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            writer.write(String.format(Locale.US, "%s,%d", operation, duration));
            writer.newLine();
        } catch (IOException ignore) {}
    }

    // пользователь

    @Test
    @Order(1)
    void user_crud_operations() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < RECORDS_COUNT; i++) {
            User user = new User("test_user_" + i, "password_" + i);
            if (i % 10 == 0) {
                user.setRole("ADMIN");
            }
            user = userRepository.save(user);
            createdUsers.add(user);
            if (i % 1000 == 0) userRepository.flush();
        }
        userRepository.flush();
        long duration = System.currentTimeMillis() - startTime;
        appendResult("user_create", duration);

        startTime = System.currentTimeMillis();
        for (User user : createdUsers) {
            userRepository.findById(user.getId());
        }
        duration = System.currentTimeMillis() - startTime;
        appendResult("user_find_by_id", duration);

        startTime = System.currentTimeMillis();
        for (User user : createdUsers) {
            userRepository.findByLogin(user.getLogin());
        }
        duration = System.currentTimeMillis() - startTime;
        appendResult("user_find_by_login", duration);

        startTime = System.currentTimeMillis();
        for (User user : createdUsers) {
            user.setLogin("updated_" + user.getLogin());
            userRepository.save(user);
        }
        userRepository.flush();
        duration = System.currentTimeMillis() - startTime;
        appendResult("user_update", duration);
    }

    // функции

    @Test
    @Order(2)
    void function_crud_operations() {
        long startTime = System.currentTimeMillis();
        int count = 0;
        for (User user : createdUsers) {
            Function function = new Function(user.getId(), "function_" + count, "signature_" + count);
            function = functionRepository.save(function);
            createdFunctions.add(function);
            if (++count % 1000 == 0) functionRepository.flush();
        }
        functionRepository.flush();
        long duration = System.currentTimeMillis() - startTime;
        appendResult("function_create", duration);

        startTime = System.currentTimeMillis();
        for (Function function : createdFunctions) {
            functionRepository.findById(function.getId());
        }
        duration = System.currentTimeMillis() - startTime;
        appendResult("function_find_by_id", duration);

        startTime = System.currentTimeMillis();
        for (User user : createdUsers) {
            functionRepository.findByUserId(user.getId());
        }
        duration = System.currentTimeMillis() - startTime;
        appendResult("function_find_by_user_id", duration);

        startTime = System.currentTimeMillis();
        for (Function function : createdFunctions) {
            function.setName("updated_" + function.getName());
            functionRepository.save(function);
        }
        functionRepository.flush();
        duration = System.currentTimeMillis() - startTime;
        appendResult("function_update", duration);
    }

    // точки

    @Test
    @Order(3)
    void point_crud_operations() {
        long startTime = System.currentTimeMillis();
        int count = 0;
        for (Function function : createdFunctions) {
            for (int j = 0; j < 3; j++) {
                Point point = new Point(function.getId(), random.nextDouble() * 100, random.nextDouble() * 100);
                point = pointRepository.save(point);
                createdPoints.add(point);
            }
            if (++count % 1000 == 0) pointRepository.flush();
        }
        pointRepository.flush();
        long duration = System.currentTimeMillis() - startTime;
        appendResult("point_create", duration);

        startTime = System.currentTimeMillis();
        for (Point point : createdPoints) {
            pointRepository.findById(point.getId());
        }
        duration = System.currentTimeMillis() - startTime;
        appendResult("point_find_by_id", duration);

        startTime = System.currentTimeMillis();
        for (Function function : createdFunctions) {
            pointRepository.findByFunctionId(function.getId());
        }
        duration = System.currentTimeMillis() - startTime;
        appendResult("point_find_by_function_id", duration);

        startTime = System.currentTimeMillis();
        for (Point point : createdPoints) {
            point.setX(point.getX() + 1.0);
            pointRepository.save(point);
        }
        pointRepository.flush();
        duration = System.currentTimeMillis() - startTime;
        appendResult("point_update", duration);
    }

    // удаление

    @Test
    @Order(4)
    void delete_operations() {
        long startTime = System.currentTimeMillis();
        pointRepository.deleteAll();
        pointRepository.flush();
        long duration = System.currentTimeMillis() - startTime;
        appendResult("point_delete_all", duration);

        startTime = System.currentTimeMillis();
        functionRepository.deleteAll();
        functionRepository.flush();
        duration = System.currentTimeMillis() - startTime;
        appendResult("function_delete_all", duration);

        startTime = System.currentTimeMillis();
        userRepository.deleteAll();
        userRepository.flush();
        duration = System.currentTimeMillis() - startTime;
        appendResult("user_delete_all", duration);
    }
}