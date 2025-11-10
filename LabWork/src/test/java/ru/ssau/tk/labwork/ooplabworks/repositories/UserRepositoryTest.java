package ru.ssau.tk.labwork.ooplabworks.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.ssau.tk.labwork.ooplabworks.entities.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void testSaveAndFindUser() {
        User user = new User("testuser", "password123");
        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getLogin());
        assertEquals("civil", foundUser.get().getRole());
    }

    @Test
    void testFindByLogin() {
        User user = new User("uniqueuser", "password");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByLogin("uniqueuser");

        assertTrue(foundUser.isPresent());
        assertEquals("uniqueuser", foundUser.get().getLogin());
    }

    @Test
    void testFindByLoginContaining() {
        userRepository.save(new User("john_doe", "pass1"));
        userRepository.save(new User("jane_doe", "pass2"));
        userRepository.save(new User("bob_smith", "pass3"));

        List<User> users = userRepository.findByLoginContaining("doe");

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getLogin().equals("john_doe")));
        assertTrue(users.stream().anyMatch(u -> u.getLogin().equals("jane_doe")));
    }

    @Test
    void testFindByRole() {
        User admin = new User("admin", "adminpass");
        admin.setRole("admin");
        userRepository.save(admin);

        userRepository.save(new User("user1", "pass1"));

        List<User> admins = userRepository.findByRole("admin");
        List<User> civilians = userRepository.findByRole("civil");

        assertEquals(1, admins.size());
        assertEquals("admin", admins.get(0).getLogin());
        assertEquals(1, civilians.size());
    }

    @Test
    void testDeleteUser() {
        User user = new User("todelete", "password");
        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());

        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }
}