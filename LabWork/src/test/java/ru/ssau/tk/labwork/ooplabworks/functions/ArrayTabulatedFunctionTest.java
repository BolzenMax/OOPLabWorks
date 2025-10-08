package ru.ssau.tk.labwork.ooplabworks.functions;

import ru.ssau.tk.labwork.ooplabworks.exceptions.InterpolationException;
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
    public void testSinglePointFunctionn() {
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

    @Test
    public void testGetXWithInvalidIndex() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {1.0, 4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertThrows(IndexOutOfBoundsException.class, () -> function.getX(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> function.getX(2));
    }

    @Test
    public void testGetYWithInvalidIndex() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {1.0, 4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertThrows(IndexOutOfBoundsException.class, () -> function.getY(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> function.getY(2));
    }

    @Test
    public void testSetYWithInvalidIndex() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {1.0, 4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertThrows(IndexOutOfBoundsException.class, () -> function.setY(-1, 5.0));
        assertThrows(IndexOutOfBoundsException.class, () -> function.setY(2, 5.0));
    }


    @Test
    public void testApplyWithExactXValues() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(1.0, function.apply(1.0));
        assertEquals(4.0, function.apply(2.0));
        assertEquals(9.0, function.apply(3.0));
    }

    @Test
    public void testSinglePointFunction() {
        double[] xValues = {1.0};
        double[] yValues = {5.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Для любой точки x должна возвращаться единственное значение y
        assertEquals(5.0, function.apply(0.0), 1e-9);   // слева от единственной точки
        assertEquals(5.0, function.apply(1.0), 1e-9);   // в самой точке
        assertEquals(5.0, function.apply(2.0), 1e-9);   // справа от единственной точки
    }


    @Test
    public void testInterpolateAtExactPoints() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Проверка в точках таблицы (должны возвращаться точные значения)
        assertEquals(1.0, function.apply(1.0), 1e-9);
        assertEquals(4.0, function.apply(2.0), 1e-9);
        assertEquals(9.0, function.apply(3.0), 1e-9);
    }

    @Test
    public void interpolate() {
        double[] xValues = {0.0, 1.0, 3.0, 6.0, 8.0, 10.0};
        double[] yValues = {0.0, 2.0, 6.0, 12.0, 16.0, 20.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);


        assertEquals(20.0, function.interpolate(10, 6), 1e-9);
        assertEquals(-2.0, function.interpolate(-1, 0), 1e-9);

        double[] xValues1 = {1.0};
        double[] yValues1 = {2.0};
        ArrayTabulatedFunction function1 = new ArrayTabulatedFunction(xValues1, yValues1);
        assertEquals(2.0, function1.interpolate(12, 213124), 1e-9);


        assertThrows(InterpolationException.class, () -> function.interpolate(1.5, 2));
        assertThrows(InterpolationException.class, () -> function.interpolate(2, 12));
        assertThrows(InterpolationException.class, () -> function.interpolate(2, -3));
        assertThrows(InterpolationException.class, () -> function.interpolate(3.5, 1 ));

    }

    @Test
    public void testInterpolateDifferentIntervals() {
        double[] xValues = {0.0, 1.0, 3.0, 6.0}; // неравномерная сетка
        double[] yValues = {0.0, 2.0, 8.0, 18.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Проверка интерполяции на разных интервалах
        assertEquals(1.0, function.apply(0.5), 1e-9);   // первый интервал [0,1]
        assertEquals(5.0, function.apply(2.0), 1e-9);   // второй интервал [1,3]
        assertEquals(13.0, function.apply(4.5), 1e-9);  // третий интервал [3,6]
    }

    @Test
    public void testExtrapolateLeftAndRight() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        double leftExtrapolated = function.apply(0.0);
        assertEquals(-2.0, leftExtrapolated, 1e-9);

        double rightExtrapolated = function.apply(4.0);
        assertEquals(14.0, rightExtrapolated, 1e-9);

    }

    @Test
    public void testInterpolateBetweenPoints() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        double interpolated = function.apply(1.5);
        assertEquals(2.5, interpolated, 1e-9);

        interpolated = function.apply(2.5);
        assertEquals(6.5, interpolated, 1e-9);
    }

    @Test
    public void testTwoPointFunction() {
        double[] xValues = {1.0, 3.0};
        double[] yValues = {2.0, 6.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Проверка всех случаев для функции из двух точек
        assertEquals(0.0, function.apply(0.0), 1e-9);   // экстраполяция слева
        assertEquals(2.0, function.apply(1.0), 1e-9);   // первая точка
        assertEquals(4.0, function.apply(2.0), 1e-9);   // интерполяция между
        assertEquals(6.0, function.apply(3.0), 1e-9);   // вторая точка
        assertEquals(8.0, function.apply(4.0), 1e-9);   // экстраполяция справа
    }


    @Test
    public void testInsert() { // вставка в начало
        double[] xValues = {2.0, 3.0, 4.0};
        double[] yValues = {4.0, 9.0, 16.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.insert(1.0, 1.0);

        assertEquals(4, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(1.0, function.getY(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(4.0, function.getY(1));
    }

    @Test
    public void testInsertInMiddle() { // вставка в середину
        double[] xValues = {1.0, 3.0, 4.0};
        double[] yValues = {1.0, 9.0, 16.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.insert(2.0, 4.0);

        assertEquals(4, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(4.0, function.getY(1));
        assertEquals(3.0, function.getX(2));
    }

    @Test
    public void testInsertAtEnd() { // вставка в конец
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.insert(4.0, 16.0);

        assertEquals(4, function.getCount());
        assertEquals(3.0, function.getX(2));
        assertEquals(4.0, function.getX(3));
        assertEquals(16.0, function.getY(3));
    }

    @Test
    public void testInsertDuplicateX() { // вставка дубликата х
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.insert(2.0, 5.0);

        assertEquals(3, function.getCount()); // Количество не должно измениться
        assertEquals(2.0, function.getX(1));
        assertEquals(5.0, function.getY(1)); // Y должно обновиться
    }

    @Test
    public void testInsertIntoEmptyFunction() { // вставка в ф-ю из одной точки
        double[] xValues = {2.0};
        double[] yValues = {4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.insert(1.0, 1.0);

        assertEquals(2, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(1.0, function.getY(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(4.0, function.getY(1));
    }

    @Test
    public void testInsertMultiplePoints() { // несколько вставок
        double[] xValues = {1.0, 4.0};
        double[] yValues = {1.0, 16.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.insert(2.0, 4.0);
        function.insert(3.0, 9.0);

        assertEquals(4, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(3.0, function.getX(2));
        assertEquals(4.0, function.getX(3));

        assertEquals(1.0, function.getY(0));
        assertEquals(4.0, function.getY(1));
        assertEquals(9.0, function.getY(2));
        assertEquals(16.0, function.getY(3));
    }

    @Test
    public void testInsertMaintainOrder() { // вставка в нужном порядке
        double[] xValues = {1.0, 5.0};
        double[] yValues = {1.0, 25.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.insert(3.0, 9.0);
        function.insert(2.0, 4.0);
        function.insert(4.0, 16.0);

        assertEquals(5, function.getCount());
        assertEquals(1.0, function.getX(0), 1e-9);
        assertEquals(2.0, function.getX(1), 1e-9);
        assertEquals(3.0, function.getX(2), 1e-9);
        assertEquals(4.0, function.getX(3), 1e-9);
        assertEquals(5.0, function.getX(4), 1e-9);

        assertEquals(1.0, function.getY(0), 1e-9);
        assertEquals(4.0, function.getY(1), 1e-9);
        assertEquals(9.0, function.getY(2), 1e-9);
        assertEquals(16.0, function.getY(3), 1e-9);
        assertEquals(25.0, function.getY(4), 1e-9);
    }

    @Test
    public void testInsertAtVeryBeginning() { // х меньше имеющихся
        double[] xValues = {10.0, 20.0, 30.0};
        double[] yValues = {100.0, 400.0, 900.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.insert(5.0, 25.0);

        assertEquals(4, function.getCount());
        assertEquals(5.0, function.getX(0));
        assertEquals(25.0, function.getY(0));
        assertEquals(10.0, function.getX(1));
    }

    @Test
    public void testInsertAtVeryEnd() { // х больше имеющихся
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.insert(5.0, 25.0);

        assertEquals(4, function.getCount());
        assertEquals(3.0, function.getX(2));
        assertEquals(5.0, function.getX(3));
        assertEquals(25.0, function.getY(3));
    }

    @Test
    public void testInsertIntoTwoPointFunction() { // ф-я из двух точек
        double[] xValues = {1.0, 3.0};
        double[] yValues = {1.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.insert(2.0, 4.0);

        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0));
        assertEquals(2.0, function.getX(1));
        assertEquals(3.0, function.getX(2));
        assertEquals(4.0, function.getY(1));
    }
}