package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SqrFunctionTest {

    @Test
    void testApplyWithPositiveNumbers() {
        MathFunction sqrFunction = new SqrFunction();

        assertEquals(0.0, sqrFunction.apply(0.0), 0.0001);
        assertEquals(1.0, sqrFunction.apply(1.0), 0.0001);
        assertEquals(4.0, sqrFunction.apply(2.0), 0.0001);
        assertEquals(25.0, sqrFunction.apply(5.0), 0.0001);
        assertEquals(100.0, sqrFunction.apply(10.0), 0.0001);
    }

    @Test
    void testApplyWithNegativeNumbers() {
        MathFunction sqrFunction = new SqrFunction();

        assertEquals(1.0, sqrFunction.apply(-1.0), 0.0001);
        assertEquals(4.0, sqrFunction.apply(-2.0), 0.0001);
        assertEquals(25.0, sqrFunction.apply(-5.0), 0.0001);
        assertEquals(100.0, sqrFunction.apply(-10.0), 0.0001);
    }

    @Test
    void testApplyWithFractionalNumbers() {
        MathFunction sqrFunction = new SqrFunction();

        assertEquals(0.25, sqrFunction.apply(0.5), 0.0001);
        assertEquals(0.25, sqrFunction.apply(-0.5), 0.0001);
        assertEquals(2.25, sqrFunction.apply(1.5), 0.0001);
        assertEquals(0.01, sqrFunction.apply(0.1), 0.0001);
    }

    @Test
    void testApplyWithLargeNumbers() {
        MathFunction sqrFunction = new SqrFunction();

        assertEquals(1_000_000.0, sqrFunction.apply(1000.0), 0.0001);
        assertEquals(1_000_000.0, sqrFunction.apply(-1000.0), 0.0001);
    }

    @Test
    void testApplyWithSpecialValues() {
        MathFunction sqrFunction = new SqrFunction();

        assertEquals(Double.POSITIVE_INFINITY, sqrFunction.apply(Double.POSITIVE_INFINITY));
        assertEquals(Double.POSITIVE_INFINITY, sqrFunction.apply(Double.NEGATIVE_INFINITY));
        assertTrue(Double.isNaN(sqrFunction.apply(Double.NaN)));
    }

    @Test
    void testApplySymmetry() {
        MathFunction sqrFunction = new SqrFunction();

        assertEquals(sqrFunction.apply(3.0), sqrFunction.apply(-3.0), 0.0001);
        assertEquals(sqrFunction.apply(7.5), sqrFunction.apply(-7.5), 0.0001);
    }
}