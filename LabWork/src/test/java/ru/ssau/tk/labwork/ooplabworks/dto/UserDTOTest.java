package ru.ssau.tk.labwork.ooplabworks.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void testConstructorAndGetters() {
        UserDTO dto = new UserDTO(10, "login", "ADMIN", "pass", true);

        assertEquals(10, dto.getId());
        assertEquals("login", dto.getLogin());
        assertEquals("ADMIN", dto.getRole());
        assertEquals("pass", dto.getPassword());
        assertTrue(dto.isEnabled());
    }

    @Test
    void testSetters() {
        UserDTO dto = new UserDTO("a", "CIVIL", "123", false);

        dto.setId(5);
        dto.setLogin("newLogin");
        dto.setRole("ROOT");
        dto.setPassword("pass2");
        dto.setEnabled(true);

        assertEquals(5, dto.getId());
        assertEquals("newLogin", dto.getLogin());
        assertEquals("ROOT", dto.getRole());
        assertEquals("pass2", dto.getPassword());
        assertTrue(dto.isEnabled());
    }

    @Test
    void testToString() {
        UserDTO dto = new UserDTO(1, "aaa", "CIVIL", "pass", true);
        String s = dto.toString();

        assertTrue(s.contains("UserDTO"));
        assertTrue(s.contains("id=1"));
        assertTrue(s.contains("login='aaa'"));
    }
}
