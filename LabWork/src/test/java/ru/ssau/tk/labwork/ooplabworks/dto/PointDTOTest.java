package ru.ssau.tk.labwork.ooplabworks.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointDTOTest {

    @Test
    void testConstructorAndGetters() {
        PointDTO dto = new PointDTO(10, 5, 2.0, 4.0);

        assertEquals(10, dto.getId());
        assertEquals(5, dto.getFunctionId());
        assertEquals(2.0, dto.getX());
        assertEquals(4.0, dto.getY());
    }

    @Test
    void testSetters() {
        PointDTO dto = new PointDTO(3, 1.1, 2.2);

        dto.setId(100);
        dto.setFunctionId(77);
        dto.setX(10.5);
        dto.setY(20.5);

        assertEquals(100, dto.getId());
        assertEquals(77, dto.getFunctionId());
        assertEquals(10.5, dto.getX());
        assertEquals(20.5, dto.getY());
    }

    @Test
    void testToString() {
        PointDTO dto = new PointDTO(1, 2, 3.0, 4.0);
        String s = dto.toString();

        assertTrue(s.contains("PointDTO"));
        assertTrue(s.contains("id=1"));
        assertTrue(s.contains("functionId=2"));
    }
}
