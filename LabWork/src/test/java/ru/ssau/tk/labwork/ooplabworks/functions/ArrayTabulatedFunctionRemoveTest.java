package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayTabulatedFunctionRemoveTest {

    @Test
    public void testRemoveFirstElement() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.remove(0);

        assertEquals(3, function.getCount());
        assertEquals(2.0, function.getX(0));
        assertEquals(4.0, function.getY(0));
        assertEquals(3.0, function.getX(1));
        assertEquals(9.0, function.getY(1));
        assertEquals(4.0, function.getX(2));
        assertEquals(16.0, function.getY(2));
    }

    @Test
    public void testRemoveLastElement() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.remove(3);

        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(1.0, function.getY(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(4.0, function.getY(1));
        assertEquals(3.0, function.getX(2));
        assertEquals(9.0, function.getY(2));
    }

    @Test
    public void testRemoveMiddleElement() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.remove(1);

        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(1.0, function.getY(0));
        assertEquals(3.0, function.getX(1));
        assertEquals(9.0, function.getY(1));
        assertEquals(4.0, function.getX(2));
        assertEquals(16.0, function.getY(2));
    }

    @Test
    public void testRemoveFromTwoElementFunction() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {1.0, 4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.remove(0);

        assertEquals(1, function.getCount());
        assertEquals(2.0, function.getX(0));
        assertEquals(4.0, function.getY(0));
    }

    @Test
    public void testRemoveToSingleElement() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {1.0, 4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.remove(1);

        assertEquals(1, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(1.0, function.getY(0));
    }

    @Test
    public void testRemoveAllElements() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {1.0, 4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.remove(0);
        function.remove(0);

        assertEquals(0, function.getCount());
    }

    @Test
    public void testRemoveInvalidIndex() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertThrows(IllegalArgumentException.class, () -> function.remove(-1));
        assertThrows(IllegalArgumentException.class, () -> function.remove(3));
    }

    @Test
    public void testRemoveAndInterpolate() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.remove(1); // Удаляем точку (2.0, 4.0)

        // Проверяем интерполяцию после удаления
        assertEquals(2.5, function.apply(1.5), 1e-9); // Интерполяция между 1.0 и 3.0
        assertEquals(6.5, function.apply(2.5), 1e-9); // Интерполяция между 1.0 и 3.0
    }

    @Test
    public void testRemoveAndExtrapolate() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.remove(0); // Удаляем первую точку

        // Проверяем экстраполяцию слева
        double leftExtrapolated = function.apply(0.5);
        assertEquals(-2.0, leftExtrapolated, 1e-9);

        function.remove(function.getCount() - 1); // Удаляем последнюю точку

        // Проверяем экстраполяцию справа
        double rightExtrapolated = function.apply(4.5);
        assertEquals(14.0, rightExtrapolated, 1e-9);
    }

    @Test
    public void testExtrapolationLeftWithSinglePoint() {
        double[] xValues = {2.0};
        double[] yValues = {4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Экстраполяция слева для функции с одной точкой
        assertEquals(4.0, function.apply(0.0), 1e-9);
        assertEquals(4.0, function.apply(1.0), 1e-9);
        assertEquals(4.0, function.apply(2.0), 1e-9);
        assertEquals(4.0, function.apply(3.0), 1e-9);
    }

    @Test
    public void testExtrapolationRightWithSinglePoint() {
        double[] xValues = {2.0};
        double[] yValues = {4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Экстраполяция справа для функции с одной точкой
        assertEquals(4.0, function.apply(3.0), 1e-9);
        assertEquals(4.0, function.apply(4.0), 1e-9);
        assertEquals(4.0, function.apply(5.0), 1e-9);
    }

    @Test
    public void testExtrapolationLeftWithTwoPoints() {
        double[] xValues = {2.0, 3.0};
        double[] yValues = {4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Экстраполяция слева для функции с двумя точками
        assertEquals(-1.0, function.apply(0.0), 1e-9);
        assertEquals(1.0, function.apply(1.0), 1e-9);
        assertEquals(4.0, function.apply(2.0), 1e-9);
    }

    @Test
    public void testExtrapolationRightWithTwoPoints() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {1.0, 4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Экстраполяция справа для функции с двумя точками
        assertEquals(4.0, function.apply(2.0), 1e-9);
        assertEquals(7.0, function.apply(3.0), 1e-9);
        assertEquals(10.0, function.apply(4.0), 1e-9);
    }

    @Test
    public void testExtrapolationAfterMultipleRemovals() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0, 25.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Удаляем несколько точек
        function.remove(0); // Удаляем первую
        function.remove(function.getCount() - 1); // Удаляем последнюю
        function.remove(1); // Удаляем из середины

        assertEquals(2, function.getCount());

        // Проверяем экстраполяцию после удалений
        double leftExtrapolated = function.apply(0.5);
        double rightExtrapolated = function.apply(4.5);

        assertTrue(Double.isFinite(leftExtrapolated));
        assertTrue(Double.isFinite(rightExtrapolated));
    }

    @Test
    public void testRemoveAndInsertCombination() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.remove(1); // Удаляем (2.0, 4.0)
        function.insert(2.5, 6.25); // Вставляем новую точку

        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(2.5, function.getX(1));
        assertEquals(3.0, function.getX(2));
        assertEquals(6.25, function.getY(1));

        // Проверяем интерполяцию
        assertEquals(3.625, function.apply(1.75), 1e-9);
    }
}