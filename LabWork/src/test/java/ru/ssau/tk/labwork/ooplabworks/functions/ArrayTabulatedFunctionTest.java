package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayTabulatedFunctionTest {

    @Test
    public void testConstructorWithArrays() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(3, function.getCount());  // Теперь работает через getCount()
        assertEquals(1.0, function.getX(0));
        assertEquals(1.0, function.getY(0));
        assertEquals(3.0, function.getX(2));
        assertEquals(9.0, function.getY(2));
    }

    @Test
    public void testConstructorWithFunction() {
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(new SqrFunction(), 0.0, 2.0, 3);

        assertEquals(3, function.getCount());  // Используем getCount() вместо прямого доступа
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
    @Test
    public void testConstructorWithInvalidCount() {
        // Тест на исключение при count < 2
        assertThrows(IllegalArgumentException.class, () -> {
            new ArrayTabulatedFunction(new SqrFunction(), 0.0, 2.0, 1);
        });
    }

    @Test
    public void testSinglePointFunction() {
        // Тест с одной точкой
        double[] xValues = {1.0};
        double[] yValues = {5.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(1, function.getCount());
        assertEquals(5.0, function.apply(1.0)); // точное значение
        assertEquals(5.0, function.apply(0.0)); // экстраполяция слева
        assertEquals(5.0, function.apply(2.0)); // экстраполяция справа
    }

    @Test
    public void testFloorIndexOfXEdgeCases() {
        // Тест граничных случаев для floorIndexOfX
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Точное совпадение с существующей точкой
        assertEquals(0, function.floorIndexOfX(1.0));
        assertEquals(1, function.floorIndexOfX(2.0));
        assertEquals(2, function.floorIndexOfX(3.0));
    }

    @Test
    public void testInterpolateWithSinglePoint() {
        // Тест интерполяции с одной точкой
        double[] xValues = {2.0};
        double[] yValues = {4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Все значения должны возвращать 4.0
        assertEquals(4.0, function.apply(1.0));
        assertEquals(4.0, function.apply(2.0));
        assertEquals(4.0, function.apply(3.0));
    }

    @Test
    public void testConstructorWithEqualBounds() {
        // Тест конструктора, когда xFrom == xTo
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(new SqrFunction(), 2.0, 2.0, 3);

        assertEquals(3, function.getCount());
        assertEquals(2.0, function.getX(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(2.0, function.getX(2));
        assertEquals(4.0, function.getY(0));
        assertEquals(4.0, function.getY(1));
        assertEquals(4.0, function.getY(2));
    }

    @Test
    public void testConstructorWithReversedBounds() {
        // Тест конструктора, когда xFrom > xTo
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(new SqrFunction(), 2.0, 0.0, 3);

        assertEquals(3, function.getCount());
        assertEquals(0.0, function.getX(0));
        assertEquals(1.0, function.getX(1));
        assertEquals(2.0, function.getX(2));
    }
}