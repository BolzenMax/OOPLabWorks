package ru.ssau.tk.labwork.ooplabworks.mapper;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;
import ru.ssau.tk.labwork.ooplabworks.models.Function;

import static org.junit.jupiter.api.Assertions.*;

class FunctionMapperTest {

    @Test
    void testToDTO() {
        Function f = new Function(10, 5, "f(x)", "x^2+1");

        FunctionDTO dto = FunctionMapper.toDTO(f);

        assertNotNull(dto);
        assertEquals(10, dto.getId());
        assertEquals(5, dto.getUserId());
        assertEquals("f(x)", dto.getName());
        assertEquals("x^2+1", dto.getSignature());
    }

    @Test
    void testFromDTO() {
        FunctionDTO dto = new FunctionDTO(20, 99, "g(x)", "sin(x)");

        Function f = FunctionMapper.fromDTO(dto);

        assertNotNull(f);
        assertEquals(20, f.getId());
        assertEquals(99, f.getUserId());
        assertEquals("g(x)", f.getName());
        assertEquals("sin(x)", f.getSignature());
    }

    @Test
    void testNullToDTO() {
        assertNull(FunctionMapper.toDTO(null));
    }

    @Test
    void testNullFromDTO() {
        assertNull(FunctionMapper.fromDTO(null));
    }
}
