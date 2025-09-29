package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ZeroFunctionTest {

    @Test
    void testApply() {
        ZeroFunction function = new ZeroFunction();

        assertEquals(0.0, function.apply(0.0));
        assertEquals(0.0, function.apply(10.0));
        assertEquals(0.0, function.apply(-5.0));
        assertEquals(0.0, function.apply(100.0));
    }

    @Test
    void testEdgeCases() {
        ZeroFunction function = new ZeroFunction();

        assertEquals(0.0, function.apply(Double.MAX_VALUE));
        assertEquals(0.0, function.apply(Double.MIN_VALUE));
        assertEquals(0.0, function.apply(Double.POSITIVE_INFINITY));
        assertEquals(0.0, function.apply(Double.NEGATIVE_INFINITY));
    }
}