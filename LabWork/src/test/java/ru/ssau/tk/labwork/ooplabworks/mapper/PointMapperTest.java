package ru.ssau.tk.labwork.ooplabworks.mapper;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.dto.PointDTO;
import ru.ssau.tk.labwork.ooplabworks.models.Point;

import static org.junit.jupiter.api.Assertions.*;

class PointMapperTest {

    @Test
    void testToDTO() {
        Point p = new Point(1, 7, 2.5, 4.5);

        PointDTO dto = PointMapper.toDTO(p);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals(7, dto.getFunctionId());
        assertEquals(2.5, dto.getX());
        assertEquals(4.5, dto.getY());
    }

    @Test
    void testFromDTO() {
        PointDTO dto = new PointDTO(3, 8.0, 9.0);
        dto.setId(200);

        Point p = PointMapper.fromDTO(dto);

        assertNotNull(p);
        assertEquals(200, p.getId());
        assertEquals(3, p.getFunctionId());
        assertEquals(8.0, p.getX());
        assertEquals(9.0, p.getY());
    }

    @Test
    void testNullToDTO() {
        assertNull(PointMapper.toDTO(null));
    }

    @Test
    void testNullFromDTO() {
        assertNull(PointMapper.fromDTO(null));
    }
}
