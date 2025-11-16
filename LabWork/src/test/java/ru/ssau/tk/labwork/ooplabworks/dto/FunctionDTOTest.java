package ru.ssau.tk.labwork.ooplabworks.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FunctionDTOTest {

    @Test
    void testConstructorAndGetters() {
        FunctionDTO dto = new FunctionDTO(10, 5, "f", "x^2");

        assertEquals(10, dto.getId());
        assertEquals(5, dto.getUserId());
        assertEquals("f", dto.getName());
        assertEquals("x^2", dto.getSignature());
    }

    @Test
    void testSetters() {
        FunctionDTO dto = new FunctionDTO(1, "g", "sin(x)");
        dto.setId(100);
        dto.setUserId(77);
        dto.setName("newName");
        dto.setSignature("cos(x)");

        assertEquals(100, dto.getId());
        assertEquals(77, dto.getUserId());
        assertEquals("newName", dto.getName());
        assertEquals("cos(x)", dto.getSignature());
    }

    @Test
    void testToString() {
        FunctionDTO dto = new FunctionDTO(1, 2, "f", "x");
        String s = dto.toString();

        assertTrue(s.contains("FunctionDTO"));
        assertTrue(s.contains("id=1"));
        assertTrue(s.contains("userId=2"));
        assertTrue(s.contains("name='f'"));
    }
}
