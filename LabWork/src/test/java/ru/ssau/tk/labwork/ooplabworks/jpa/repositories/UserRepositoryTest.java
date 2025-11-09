package ru.ssau.tk.labwork.ooplabworks.jpa.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.ssau.tk.labwork.ooplabworks.jpa.entities.User;

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
        // Очищаем базу перед и после каждого теста
        userRepository.deleteAll();
    }

    @Test
    void testSaveAndFindUser() {
        // Создание и сохранение пользователя
        User user = new User("testuser", "password123");
        User savedUser = userRepository.save(user);

        // Поиск по ID
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getLogin());
        assertEquals("civil", foundUser.get().getRole());
    }

    @Test
    void testFindByLogin() {
        // Подготовка данных
        User user = new User("uniqueuser", "password");
        userRepository.save(user);

        // Поиск по логину
        Optional<User> foundUser = userRepository.findByLogin("uniqueuser");

        assertTrue(foundUser.isPresent());
        assertEquals("uniqueuser", foundUser.get().getLogin());
    }

    @Test
    void testFindByLoginContaining() {
        // Подготовка данных
        userRepository.save(new User("john_doe", "pass1"));
        userRepository.save(new User("jane_doe", "pass2"));
        userRepository.save(new User("bob_smith", "pass3"));

        // Поиск по части логина
        List<User> users = userRepository.findByLoginContaining("doe");

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getLogin().equals("john_doe")));
        assertTrue(users.stream().anyMatch(u -> u.getLogin().equals("jane_doe")));
    }

    @Test
    void testFindByRole() {
        // Подготовка данных
        User admin = new User("admin", "adminpass");
        admin.setRole("admin");
        userRepository.save(admin);

        userRepository.save(new User("user1", "pass1"));

        // Поиск по роли
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

        // Удаляем пользователя
        userRepository.deleteById(savedUser.getId());

        // Проверяем, что пользователь удален
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }
}