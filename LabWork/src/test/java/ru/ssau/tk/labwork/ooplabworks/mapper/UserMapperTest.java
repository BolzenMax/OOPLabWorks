package ru.ssau.tk.labwork.ooplabworks.mapper;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;
import ru.ssau.tk.labwork.ooplabworks.models.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void testToDTO() {
        User user = new User(1, "login", "ADMIN", "pass", true);

        UserDTO dto = UserMapper.toDTO(user);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("login", dto.getLogin());
        assertEquals("ADMIN", dto.getRole());
        assertEquals("pass", dto.getPassword());
        assertTrue(dto.isEnabled());
    }

    @Test
    void testFromDTO() {
        UserDTO dto = new UserDTO(2, "user", "CIVIL", "pwd", false);

        User model = UserMapper.fromDTO(dto);

        assertNotNull(model);
        assertEquals(2, model.getId());
        assertEquals("user", model.getLogin());
        assertEquals("CIVIL", model.getRole());
        assertEquals("pwd", model.getPassword());
        assertFalse(model.isEnabled());
    }

    @Test
    void testNullToDTO() {
        assertNull(UserMapper.toDTO(null));
    }

    @Test
    void testNullFromDTO() {
        assertNull(UserMapper.fromDTO(null));
    }
}
