package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayTabulatedFunctionTest {

    @Test
    public void testConstructorWithArrays() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(1.0, function.getY(0));
        assertEquals(3.0, function.getX(2));
        assertEquals(9.0, function.getY(2));
    }

    @Test
    public void testConstructorWithFunction() {
        MathFunction sqr = new SqrFunction();
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(sqr, 0.0, 2.0, 3);

        assertEquals(3, function.getCount());
        assertEquals(0.0, function.getX(0));
        assertEquals(1.0, function.getX(1));
        assertEquals(2.0, function.getX(2));
        assertEquals(0.0, function.getY(0));
        assertEquals(1.0, function.getY(1));
        assertEquals(4.0, function.getY(2));
    }

    @Test
    public void testGetSetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 2.0, 3.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.setY(1, 5.0);
        assertEquals(5.0, function.getY(1));
    }

    @Test
    public void testIndexOf() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(1, function.indexOfX(2.0));
        assertEquals(-1, function.indexOfX(5.0));
        assertEquals(1, function.indexOfY(4.0));
        assertEquals(-1, function.indexOfY(5.0));
    }

    @Test
    public void testBounds() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(1.0, function.leftBound());
        assertEquals(3.0, function.rightBound());
    }

    @Test
    public void testApply() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Точное значение
        assertEquals(4.0, function.apply(2.0));

        // Интерполяция
        double interpolated = function.apply(1.5);
        assertTrue(interpolated > 1.0 && interpolated < 4.0);

        // Экстраполяция слева
        double leftExtrapolated = function.apply(0.5);
        assertTrue(leftExtrapolated < 1.0);

        // Экстраполяция справа
        double rightExtrapolated = function.apply(3.5);
        assertTrue(rightExtrapolated > 9.0);
    }

    @Test
    public void testFloorIndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(0, function.floorIndexOfX(0.5));   // слева
        assertEquals(0, function.floorIndexOfX(1.5));   // между 1 и 2
        assertEquals(1, function.floorIndexOfX(2.5));   // между 2 и 3
        assertEquals(3, function.floorIndexOfX(3.5));   // справа
    }
}