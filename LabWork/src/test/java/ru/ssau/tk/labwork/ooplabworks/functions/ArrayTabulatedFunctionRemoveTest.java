package ru.ssau.tk.labwork.ooplabworks.functions;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayTabulatedFunctionRemoveTest {

    @Test
    public void testConstructorWithArrays() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(3, function.getCount());
        assertEquals(1.0, function.leftBound(), 1e-9);
        assertEquals(3.0, function.rightBound(), 1e-9);
    }

    @Test
    public void testInterpolation() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(2.5, function.apply(1.5), 1e-9);
        assertEquals(6.5, function.apply(2.5), 1e-9);
    }

    @Test
    public void testExtrapolation() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(-2.0, function.apply(0.0), 1e-9);  // слева
        assertEquals(14.0, function.apply(4.0), 1e-9);  // справа
    }
}
