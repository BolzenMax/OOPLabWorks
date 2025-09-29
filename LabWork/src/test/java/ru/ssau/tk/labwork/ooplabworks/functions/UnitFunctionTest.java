package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnitFunctionTest {

    @Test
    void testApply() {
        UnitFunction function = new UnitFunction();

        assertEquals(1.0, function.apply(0.0));
        assertEquals(1.0, function.apply(10.0));
        assertEquals(1.0, function.apply(-5.0));
        assertEquals(1.0, function.apply(100.0));
    }

    @Test
    void testEdgeCases() {
        UnitFunction function = new UnitFunction();

        assertEquals(1.0, function.apply(Double.MAX_VALUE));
        assertEquals(1.0, function.apply(Double.MIN_VALUE));
        assertEquals(1.0, function.apply(Double.POSITIVE_INFINITY));
        assertEquals(1.0, function.apply(Double.NEGATIVE_INFINITY));
    }
}