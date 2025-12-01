package ru.ssau.tk.labwork.ooplabworks.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ssau.tk.labwork.ooplabworks.entities.User;
import ru.ssau.tk.labwork.ooplabworks.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User adminUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setLogin("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRole("CIVIL");
        testUser.setEnabled(true);

        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setLogin("admin");
        adminUser.setPassword("adminEncoded");
        adminUser.setRole("ADMIN");
        adminUser.setEnabled(true);
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser(testUser);

        assertNotNull(result);
        assertEquals("testuser", result.getLogin());
        assertEquals("CIVIL", result.getRole());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getLogin());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(999L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void testGetUserByLogin_Found() {
        when(userRepository.findByLogin("testuser")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getUserByLogin("testuser");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(userRepository, times(1)).findByLogin("testuser");
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(testUser, adminUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("testuser", result.get(0).getLogin());
        assertEquals("admin", result.get(1).getLogin());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        testUser.setLogin("updatedUser");
        when(userRepository.save(testUser)).thenReturn(testUser);

        User result = userService.updateUser(testUser);

        assertEquals("updatedUser", result.getLogin());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUserExists_True() {
        when(userRepository.existsByLogin("testuser")).thenReturn(true);

        boolean result = userService.userExists("testuser");

        assertTrue(result);
        verify(userRepository, times(1)).existsByLogin("testuser");
    }

    @Test
    void testUserExists_False() {
        when(userRepository.existsByLogin("nonexistent")).thenReturn(false);

        boolean result = userService.userExists("nonexistent");

        assertFalse(result);
    }
}
