package ru.ssau.tk.labwork.ooplabworks.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.ssau.tk.labwork.ooplabworks.entities.Function;
import ru.ssau.tk.labwork.ooplabworks.entities.Point;
import ru.ssau.tk.labwork.ooplabworks.entities.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class PointRepositoryTest {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @AfterEach
    void cleanup() {
        pointRepository.deleteAll();
        functionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSaveAndFindPoint() {
        Function testFunction = createTestFunction();
        Point point = new Point(testFunction.getId(), 1.0, 2.0);
        Point savedPoint = pointRepository.save(point);

        assertNotNull(savedPoint.getId());
        assertEquals(1.0, savedPoint.getX());
        assertEquals(2.0, savedPoint.getY());
        assertEquals(testFunction.getId(), savedPoint.getFunctionId());
    }

    @Test
    void testFindByFunctionId() {
        User user = new User("testuser", "password", "civil", true);
        userRepository.save(user);

        Function function = new Function(user.getId(), "testFunc", "double f(double x)");
        functionRepository.save(function);

        Point point = new Point(function.getId(), 1.0, 2.0);
        pointRepository.save(point);

        List<Point> foundPoints = pointRepository.findByFunctionId(function.getId());

        assertEquals(1, foundPoints.size());
        assertEquals(function.getId(), foundPoints.get(0).getFunctionId());
        assertEquals(1.0, foundPoints.get(0).getX());
        assertEquals(2.0, foundPoints.get(0).getY());
    }

    @Test
    void testFindByFunctionIdAndXBetween() {
        Function testFunction = createTestFunction();

        pointRepository.save(new Point(testFunction.getId(), 1.0, 1.0));
        pointRepository.save(new Point(testFunction.getId(), 2.0, 4.0));
        pointRepository.save(new Point(testFunction.getId(), 3.0, 9.0));
        pointRepository.save(new Point(testFunction.getId(), 4.0, 16.0));

        List<Point> pointsInRange = pointRepository.findByFunctionIdAndXBetween(testFunction.getId(), 2.0, 3.0);

        assertEquals(2, pointsInRange.size());
        assertTrue(pointsInRange.stream().allMatch(p -> p.getX() >= 2.0 && p.getX() <= 3.0));
    }

    @Test
    void testDeletePoint() {
        Function testFunction = createTestFunction();
        Point point = new Point(testFunction.getId(), 5.0, 25.0);
        Point savedPoint = pointRepository.save(point);

        pointRepository.deleteById(savedPoint.getId());

        Optional<Point> deletedPoint = pointRepository.findById(savedPoint.getId());
        assertFalse(deletedPoint.isPresent());
    }

    @Test
    void testBatchOperations() {
        Function testFunction = createTestFunction();

        List<Point> points = List.of(
                new Point(testFunction.getId(), 1.0, 1.0),
                new Point(testFunction.getId(), 2.0, 4.0),
                new Point(testFunction.getId(), 3.0, 9.0)
        );

        List<Point> savedPoints = pointRepository.saveAll(points);
        assertEquals(3, savedPoints.size());

        // Пакетное чтение
        List<Point> allPoints = pointRepository.findAll();
        assertEquals(3, allPoints.size());
    }

    private Function createTestFunction() {
        return createFunction("testFunc", "double f(double x)");
    }

    private Function createFunction(String name, String signature) {
        User user = createUser("testuser", "password", "civil", true);
        Function function = new Function(user.getId(), name, signature);
        return functionRepository.save(function);
    }

    private User createUser(String login, String password, String role, boolean enabled) {
        User user = new User(login, password, role, enabled);
        return userRepository.save(user);
    }
}