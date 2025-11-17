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
import ru.ssau.tk.labwork.ooplabworks.services.SearchSortService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SortPerformanceTest {

    private static final int ITERATIONS = 100;
    private static final String CSV_FILE = "SortPerformanceFramework.csv";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private SearchSortService searchSortService;

    private static boolean dataPrepared = false;
    private static Long testUserId;
    private static Long testFunctionId;
    private static String testUserName;
    private static Double testXValue;

    // таблица

    @BeforeAll
    static void setupCsv() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            writer.write("Операция,Время (мс)");
            writer.newLine();
        }
    }

    private void appendResult(String method, double avgMs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            writer.write(String.format(Locale.US, "%s,%.2f", method, avgMs));
            writer.newLine();
        } catch (IOException ignore) {}
    }

    // подготовка

    private void prepareDataOnce() {
        if (dataPrepared) return;

        // очистка

        pointRepository.deleteAll();
        functionRepository.deleteAll();
        userRepository.deleteAll();

        // создание

        Random random = new Random(42);

        for (int i = 0; i < 100; i++) { // 100 пользователей
            User user = new User("user_" + i, "password_" + i);
            if (i % 10 == 0) {
                user.setRole("ADMIN");
            }
            user = userRepository.save(user);

            if (i == 0) {
                testUserId = user.getId();
                testUserName = user.getLogin();
            }

            for (int j = 0; j < 10; j++) { // 10 функций
                Function function = new Function(user.getId(), "function_" + i + "_" + j, "signature_" + i + "_" + j);
                function = functionRepository.save(function);

                if (i == 0 && j == 0) {
                    testFunctionId = function.getId();
                }

                for (int k = 0; k < 10; k++) { // 10 точек
                    Double x = random.nextDouble() * 100;
                    Double y = Math.sin(x);
                    Point point = new Point(function.getId(), x, y);
                    pointRepository.save(point);

                    if (i == 0 && j == 0 && k == 0) {
                        testXValue = x;
                    }
                }
            }
        }

        dataPrepared = true;
    }

    @Test
    @Order(1)
    void sort_users_by_login_asc() {
        prepareDataOnce();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < ITERATIONS; i++) {
            List<User> users = searchSortService.findAllUsersSortedByLogin(true);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        double avgTime = (double) totalTime;
        appendResult("sort_users_by_login_asc", avgTime);
    }

    @Test
    @Order(2)
    void sort_users_by_login_desc() {
        prepareDataOnce();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < ITERATIONS; i++) {
            List<User> users = searchSortService.findAllUsersSortedByLogin(false);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        double avgTime = (double) totalTime;
        appendResult("sort_users_by_login_desc", avgTime);
    }

    @Test
    @Order(3)
    void sort_users_by_id_asc() {
        prepareDataOnce();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < ITERATIONS; i++) {
            List<User> users = searchSortService.findAllUsersSortedById(true);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        double avgTime = (double) totalTime;
        appendResult("sort_users_by_id_asc", avgTime);
    }

    @Test
    @Order(4)
    void sort_functions_by_name_asc() {
        prepareDataOnce();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < ITERATIONS; i++) {
            List<Function> functions = searchSortService.findAllFunctionsSortedByName(true);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        double avgTime = (double) totalTime;
        appendResult("sort_functions_by_name_asc", avgTime);
    }

    @Test
    @Order(5)
    void sort_functions_by_user_id_asc() {
        prepareDataOnce();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < ITERATIONS; i++) {
            List<Function> functions = searchSortService.findAllFunctionsSortedByUserId(true);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        double avgTime = (double) totalTime;
        appendResult("sort_functions_by_user_id_asc", avgTime);
    }

    @Test
    @Order(6)
    void sort_points_by_x_asc() {
        prepareDataOnce();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < ITERATIONS; i++) {
            List<Point> points = searchSortService.findAllPointsSortedByX(true);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        double avgTime = (double) totalTime;
        appendResult("sort_points_by_x_asc", avgTime);
    }

    @Test
    @Order(7)
    void sort_points_by_function_id_asc() {
        prepareDataOnce();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < ITERATIONS; i++) {
            List<Point> points = searchSortService.findAllPointsSortedByFunctionId(true);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        double avgTime = (double) totalTime;
        appendResult("sort_points_by_function_id_asc", avgTime);
    }

    @Test
    @Order(8)
    void sort_points_by_function_x_asc() {
        prepareDataOnce();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < ITERATIONS; i++) {
            List<Point> points = searchSortService.findPointsByFunctionIdOrderedByXAsc(testFunctionId);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        double avgTime = (double) totalTime;
        appendResult("sort_points_by_function_x_asc", avgTime);
    }

    @Test
    @Order(9)
    void sort_points_by_function_x_desc() {
        prepareDataOnce();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < ITERATIONS; i++) {
            List<Point> points = searchSortService.findPointsByFunctionIdOrderedByXDesc(testFunctionId);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        double avgTime = (double) totalTime;
        appendResult("sort_points_by_function_x_desc", avgTime);
    }
}