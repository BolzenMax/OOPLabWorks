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

    @BeforeAll
    static void setupCsv() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            writer.write("Операция,Время (мс)");
            writer.newLine();
        }
    }

    private void appendResult(String operation, double avgTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            writer.write(String.format(Locale.US, "%s,%.3f", operation, avgTime));
            writer.newLine();
        } catch (IOException ignore) {}
    }

    // пользователь

    @Test
    @Order(1)
    void user_crud_operations() {
        List<User> createdUsers = new ArrayList<>();

        long startTime = System.currentTimeMillis(); // 10000 юзеров
        for (int i = 0; i < RECORDS_COUNT; i++) {
            User user = new User("test_user_" + i, "password_" + i);
            if (i % 10 == 0) {
                user.setRole("ADMIN");
            }
            user = userRepository.save(user);
            createdUsers.add(user);
            if (i % 1000 == 0) {
                userRepository.flush();
            }
        }
        userRepository.flush();
        long totalTime = System.currentTimeMillis() - startTime;
        double avgTime = (double) totalTime;
        appendResult("user_create", avgTime);

        startTime = System.currentTimeMillis();
        for (User user : createdUsers) {
            userRepository.findById(user.getId()).orElse(null);
        }
        totalTime = System.currentTimeMillis() - startTime;
        avgTime = (double) totalTime;
        appendResult("user_find_by_id", avgTime);

        startTime = System.currentTimeMillis();
        for (User user : createdUsers) {
            userRepository.findByLogin(user.getLogin()).orElse(null);
        }
        totalTime = System.currentTimeMillis() - startTime;
        avgTime = (double) totalTime;
        appendResult("user_find_by_login", avgTime);

        startTime = System.currentTimeMillis();
        for (User user : createdUsers) {
            user.setLogin("updated_" + user.getLogin());
            userRepository.save(user);
        }
        userRepository.flush();
        totalTime = System.currentTimeMillis() - startTime;
        avgTime = (double) totalTime;
        appendResult("user_update", avgTime);
    }

    // функции

    @Test
    @Order(2)
    void function_crud_operations() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 1; i++) { // юзер
            User user = new User("func_user_" + i, "pass_" + i);
            users.add(userRepository.save(user));
        }
        userRepository.flush();

        List<Function> createdFunctions = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        int count = 0;
        for (User user : users) {
            for (int j = 0; j < 10000; j++) { // 10000 функций
                Function function = new Function(user.getId(), "function_" + count, "signature_" + count);
                function = functionRepository.save(function);
                createdFunctions.add(function);
                count++;
            }
            if (count % 1000 == 0) {
                functionRepository.flush();
            }
        }
        functionRepository.flush();
        long totalTime = System.currentTimeMillis() - startTime;
        double avgTime = (double) totalTime;
        appendResult("function_create", avgTime);

        startTime = System.currentTimeMillis();
        for (Function function : createdFunctions) {
            functionRepository.findById(function.getId()).orElse(null);
        }
        totalTime = System.currentTimeMillis() - startTime;
        avgTime = (double) totalTime;
        appendResult("function_find_by_id", avgTime);

        startTime = System.currentTimeMillis();
        for (User user : users) {
            functionRepository.findByUserId(user.getId());
        }
        totalTime = System.currentTimeMillis() - startTime;
        avgTime = (double) totalTime;
        appendResult("function_find_by_user_id", avgTime);

        startTime = System.currentTimeMillis();
        for (Function function : createdFunctions) {
            function.setName("updated_" + function.getName());
            functionRepository.save(function);
        }
        functionRepository.flush();
        totalTime = System.currentTimeMillis() - startTime;
        avgTime = (double) totalTime;
        appendResult("function_update", avgTime);
    }

    // точки

    @Test
    @Order(3)
    void point_crud_operations() {
        User user = userRepository.save(new User("point_user", "pass"));
        List<Function> functions = new ArrayList<>();
        for (int i = 0; i < 1; i++) { // функция
            Function function = new Function(user.getId(), "point_func_" + i, "sig_" + i);
            functions.add(functionRepository.save(function));
        }
        functionRepository.flush();

        List<Point> createdPoints = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        int count = 0;
        for (Function function : functions) {
            for (int j = 0; j < 10000; j++) { // 10 точек
                Point point = new Point(function.getId(), random.nextDouble() * 100, random.nextDouble() * 100);
                point = pointRepository.save(point);
                createdPoints.add(point);
                count++;
            }
            if (count % 1000 == 0) {
                pointRepository.flush();
            }
        }
        pointRepository.flush();
        long totalTime = System.currentTimeMillis() - startTime;
        double avgTime = (double) totalTime;
        appendResult("point_create", avgTime);

        startTime = System.currentTimeMillis();
        for (Point point : createdPoints) {
            pointRepository.findById(point.getId()).orElse(null);
        }
        totalTime = System.currentTimeMillis() - startTime;
        avgTime = (double) totalTime;
        appendResult("point_find_by_id", avgTime);

        startTime = System.currentTimeMillis();
        for (Function function : functions) {
            pointRepository.findByFunctionId(function.getId());
        }
        totalTime = System.currentTimeMillis() - startTime;
        avgTime = (double) totalTime;
        appendResult("point_find_by_function_id", avgTime);

        startTime = System.currentTimeMillis();
        for (Point point : createdPoints) {
            point.setX(point.getX() + 1.0);
            pointRepository.save(point);
        }
        pointRepository.flush();
        totalTime = System.currentTimeMillis() - startTime;
        avgTime = (double) totalTime;
        appendResult("point_update", avgTime);
    }

    // удаление

    @Test
    @Order(4)
    void delete_users_operations() {
        List<User> usersToDelete = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) { // 10000 человек
            User user = new User("delete_user_" + i, "password_" + i);
            usersToDelete.add(userRepository.save(user));
            if (i % 100 == 0) {
                userRepository.flush();
            }
        }
        userRepository.flush();

        startTime = System.currentTimeMillis();
        userRepository.deleteAll(usersToDelete);
        userRepository.flush();
        long duration = System.currentTimeMillis() - startTime;
        double avgTime = (double) duration;
        appendResult("user_delete_all", avgTime);
    }

    @Test
    @Order(5)
    void delete_functions_operations() {
        User user = userRepository.save(new User("delete_this_user", "pass_user"));
        List<Function> functionsToDelete = new ArrayList<>();
        for (int i = 0; i < 10000; i++) { // 100 функций
            Function function = new Function(user.getId(), "del_func_" + i, "sig");
            functionsToDelete.add(functionRepository.save(function));
        }
        functionRepository.flush();

        long startTime = System.currentTimeMillis();
        functionRepository.deleteAll(functionsToDelete);
        functionRepository.flush();
        long duration = System.currentTimeMillis() - startTime;
        double avgTime = (double) duration;
        appendResult("function_delete_all", avgTime);
    }

    @Test
    @Order(6)
    void delete_points_operations() {
        User user = userRepository.save(new User("delete_user", "pass"));
        Function function = functionRepository.save(new Function(user.getId(), "del_func_", "sig"));
        List<Function> functions = new ArrayList<>();
        List<Point> points = new ArrayList<>();

        for (int j = 0; j < 10000; j++) { // 10000 точек
            Point point = new Point(function.getId(), random.nextDouble() * 100, random.nextDouble() * 100);
            points.add(pointRepository.save(point));
        }
        pointRepository.flush();

        long startTime = System.currentTimeMillis();
        pointRepository.deleteAll(points);
        pointRepository.flush();
        long duration = System.currentTimeMillis() - startTime;
        double avgTime = (double) duration;
        appendResult("point_delete_all", avgTime);
    }
}