package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ConstantFunctionTest {

    @Test
    void testApply() {
        ConstantFunction function = new ConstantFunction(5.0);

        // Проверяем, что всегда возвращается константа
        assertEquals(5.0, function.apply(0.0));
        assertEquals(5.0, function.apply(1120));
        assertEquals(5.0, function.apply(-5.0));
        assertEquals(5.0, function.apply(1230.0));
        assertEquals(5.0, function.apply(12121212.1212));
    }

    @Test
    void testGetConstant() {
        ConstantFunction function = new ConstantFunction(3.14);
        assertEquals(3.14, function.getConstant());
    }

    @Test
    void testApplyWithDifferentConstants() {
        // Тестируем с разными значениями констант
        ConstantFunction negative = new ConstantFunction(-10.0);
        ConstantFunction zero = new ConstantFunction(0.0);
        ConstantFunction positive = new ConstantFunction(7.5);

        assertEquals(-10.0, negative.apply(5.0));
        assertEquals(0.0, zero.apply(5.0));
        assertEquals(7.5, positive.apply(5.0));
    }
}