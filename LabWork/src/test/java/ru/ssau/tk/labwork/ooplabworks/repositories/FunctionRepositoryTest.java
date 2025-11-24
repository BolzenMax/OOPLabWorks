package ru.ssau.tk.labwork.ooplabworks.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.ssau.tk.labwork.ooplabworks.entities.Function;
import ru.ssau.tk.labwork.ooplabworks.entities.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class FunctionRepositoryTest {

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @AfterEach
    void cleanup() {
        functionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSaveAndFindFunction() {
        User testUser = createTestUser();
        Function function = new Function(testUser.getId(), "testFunction", "double test(double x)");
        Function savedFunction = functionRepository.save(function);

        assertNotNull(savedFunction.getId());
        assertEquals("testFunction", savedFunction.getName());
        assertEquals(testUser.getId(), savedFunction.getUserId());
    }

    @Test
    void testFindByUserId() {
        User testUser = createTestUser();
        User otherUser = createUser("other", "pass", "civil", true);

        functionRepository.save(new Function(testUser.getId(), "func1", "double f1(double x)"));
        functionRepository.save(new Function(testUser.getId(), "func2", "double f2(double x)"));

        functionRepository.save(new Function(otherUser.getId(), "func3", "double f3(double x)"));

        List<Function> userFunctions = functionRepository.findByUserId(testUser.getId());

        assertEquals(2, userFunctions.size());
        assertTrue(userFunctions.stream().allMatch(f -> f.getUserId().equals(testUser.getId())));
    }

    @Test
    void testFindByNameContaining() {
        User testUser = createTestUser();

        functionRepository.save(new Function(testUser.getId(), "linear_function", "double linear(double x)"));
        functionRepository.save(new Function(testUser.getId(), "quadratic_function", "double quadratic(double x)"));
        functionRepository.save(new Function(testUser.getId(), "sin_function", "double sin(double x)"));

        List<Function> linearFunctions = functionRepository.findByNameContaining("linear");
        List<Function> functionFunctions = functionRepository.findByNameContaining("function");

        assertEquals(1, linearFunctions.size());
        assertEquals(3, functionFunctions.size());
    }

    @Test
    void testFindByUserIdAndNameContaining() {
        User testUser = createTestUser();
        User otherUser = createUser("other", "pass", "civil", true);

        functionRepository.save(new Function(testUser.getId(), "linear_function", "double linear(double x)"));
        functionRepository.save(new Function(testUser.getId(), "quadratic_function", "double quadratic(double x)"));
        functionRepository.save(new Function(otherUser.getId(), "linear_other", "double linear(double x)"));

        List<Function> results = functionRepository.findByUserIdAndNameContaining(testUser.getId(), "linear");

        assertEquals(1, results.size());
        assertEquals("linear_function", results.get(0).getName());
        assertEquals(testUser.getId(), results.get(0).getUserId());
    }

    @Test
    void testDeleteFunction() {
        User testUser = createTestUser();
        Function function = new Function(testUser.getId(), "todelete", "double f(double x)");
        Function savedFunction = functionRepository.save(function);

        functionRepository.deleteById(savedFunction.getId());

        Optional<Function> deletedFunction = functionRepository.findById(savedFunction.getId());
        assertFalse(deletedFunction.isPresent());
    }

    private User createTestUser() {
        return createUser("testuser", "password", "civil", true);
    }

    private User createUser(String login, String password, String role, boolean enabled) {
        User user = new User(login, password, role, enabled);
        return userRepository.save(user);
    }
}