package ru.ssau.tk.labwork.ooplabworks.functions;

import ru.ssau.tk.labwork.ooplabworks.exceptions.InterpolationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkedListTabulatedFunctionTest {

    @Test
    void testConstructorWithArrays() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};

        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(4, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(4.0, function.getX(3));
        assertEquals(20.0, function.getY(1));
        assertEquals(40.0, function.getY(3));
    }

    @Test
    void testConstructorWithThrowsException() { // маленький
        double[] xValues = {1.0};
        double[] yValues = {10.0};

        assertThrows(IllegalArgumentException.class, () -> {
            new LinkedListTabulatedFunction(xValues, yValues);
        });
    }

    @Test
    void testConstructorWithMathFunction() {
        MathFunction source = new SqrFunction();
        double xFrom = 0.0;
        double xTo = 3.0;
        int count = 4;

        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(source, xFrom, xTo, count);

        assertEquals(4, function.getCount());
        assertEquals(0.0, function.getX(0));
        assertEquals(3.0, function.getX(3));
        assertEquals(0.0, function.getY(0));
    }

    @Test
    void testConstructorWithMathFunctionReverseRange() {
        MathFunction source = new SqrFunction();
        double xFrom = 3.0;
        double xTo = 0.0;
        int count = 4;

        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(source, xFrom, xTo, count);

        assertEquals(4, function.getCount());
        assertEquals(0.0, function.getX(0));
        assertEquals(3.0, function.getX(3));
    }

    @Test
    void testConstructorWithMathFunctionEqualBounds() {
        MathFunction source = new SqrFunction();
        double xFrom = 2.0;
        double xTo = 2.0;
        int count = 3;

        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(source, xFrom, xTo, count);

        assertEquals(3, function.getCount());
        assertEquals(2.0, function.getX(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(2.0, function.getX(2));
        assertEquals(4.0, function.getY(0));
        assertEquals(4.0, function.getY(1));
        assertEquals(4.0, function.getY(2));
    }

    @Test
    void testConstructorWithMathFunctionThrowsException() {
        MathFunction source = new SqrFunction();

        assertThrows(IllegalArgumentException.class, () -> {
            new LinkedListTabulatedFunction(source, 0.0, 1.0, 1);
        });
    }

    @Test
    void testGetCount() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(2, function.getCount());
    }

    @Test
    void testLeftBound() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(1.0, function.leftBound());
    }

    @Test
    void testRightBound() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(3.0, function.rightBound());
    }

    @Test
    void testGetX() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(1.0, function.getX(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(3.0, function.getX(2));
    }

    @Test
    void testGetXThrowsException() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertThrows(IllegalArgumentException.class, () -> function.getX(-1));
        assertThrows(IllegalArgumentException.class, () -> function.getX(2));
    }

    @Test
    void testGetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(10.0, function.getY(0));
        assertEquals(20.0, function.getY(1));
        assertEquals(30.0, function.getY(2));
    }

    @Test
    void testGetYThrowsException() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertThrows(IllegalArgumentException.class, () -> function.getY(-1));
        assertThrows(IllegalArgumentException.class, () -> function.getY(2));
    }

    @Test
    void testSetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.setY(1, 25.0);
        assertEquals(25.0, function.getY(1));

        function.setY(2, 35.0);
        assertEquals(35.0, function.getY(2));
    }

    @Test
    void testSetYThrowsException() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertThrows(IllegalArgumentException.class, () -> function.setY(-1, 15.0));
        assertThrows(IllegalArgumentException.class, () -> function.setY(2, 15.0));
    }

    @Test
    void testIndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(0, function.indexOfX(1.0));
        assertEquals(2, function.indexOfX(3.0));
        assertEquals(-1, function.indexOfX(5.0));
    }

    @Test
    void testIndexOfY() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(0, function.indexOfY(10.0));
        assertEquals(2, function.indexOfY(30.0));
        assertEquals(-1, function.indexOfY(50.0));
    }

    @Test
    void testFloorIndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(0, function.floorIndexOfX(1.0));
        assertEquals(0, function.floorIndexOfX(1.5));
        assertEquals(1, function.floorIndexOfX(2.0));
        assertEquals(2, function.floorIndexOfX(3.5));
    }

    @Test
    void testFloorIndexOfXThrowException() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertThrows(IllegalArgumentException.class, () -> function.floorIndexOfX(0.5)); // x меньше
        assertThrows(IllegalArgumentException.class, () -> function.floorIndexOfX(4.5)); // х больше
    }

    @Test
    void testApplyExactMatch() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(10.0, function.apply(1.0));
        assertEquals(30.0, function.apply(3.0));
    }

    @Test
    void testApplyInterpolation() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        double result = function.apply(1.5);
        double expected = 15.0;
        assertEquals(expected, result, 1e-10);
    }

    @Test
    void testApplyExtrapolationLeft() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        double result = function.apply(0.5);
        double expected = 5.0;
        assertEquals(expected, result, 1e-10);
    }

    @Test
    void testApplyExtrapolationRight() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        double result = function.apply(3.5);
        double expected = 35.0;
        assertEquals(expected, result, 1e-10);
    }

    @Test
    void testRemoveFirst() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.remove(0);

        assertEquals(2, function.getCount());
        assertEquals(2.0, function.getX(0));
        assertEquals(3.0, function.getX(1));
        assertEquals(20.0, function.getY(0));
        assertEquals(30.0, function.getY(1));
    }

    @Test
    void testRemoveMiddle() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.remove(1);

        assertEquals(2, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(3.0, function.getX(1));
        assertEquals(10.0, function.getY(0));
        assertEquals(30.0, function.getY(1));
    }

    @Test
    void testRemoveLast() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.remove(2);

        assertEquals(2, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(10.0, function.getY(0));
        assertEquals(20.0, function.getY(1));
    }

    @Test
    void testRemoveThrowsException() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertThrows(IllegalArgumentException.class, () -> function.remove(-1));
        assertThrows(IllegalArgumentException.class, () -> function.remove(2));
    }


    @Test
    void InterpolateExceptionTest() {

        double[] xValues = {0.0, 1.0, 3.0, 6.0, 8.0, 10.0};
        double[] yValues = {0.0, 2.0, 6.0, 12.0, 16.0, 20.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(16.0, function.interpolate(8, 4), 1e-9);

        assertThrows(InterpolationException.class, () -> function.interpolate(1.5, 2));
        assertThrows(InterpolationException.class, () -> function.interpolate(2, 12));
        assertThrows(InterpolationException.class, () -> function.interpolate(2, -3));
        assertThrows(InterpolationException.class, () -> function.interpolate(3.5, 1 ));

    }
}