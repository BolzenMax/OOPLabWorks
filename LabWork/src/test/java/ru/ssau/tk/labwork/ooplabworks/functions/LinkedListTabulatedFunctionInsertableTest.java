package ru.ssau.tk.labwork.ooplabworks.functions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTabulatedFunctionInsertableTest

{
    @Test
    public void testInsertAtBeginning() {
        double[] xValues = {2.0, 3.0, 4.0};
        double[] yValues = {4.0, 9.0, 16.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.insert(1.0, 1.0);

        assertEquals(4, function.getCount());
        assertEquals(1.0, function.getX(0), 1e-9);
        assertEquals(1.0, function.getY(0), 1e-9);
        assertEquals(2.0, function.getX(1), 1e-9);
        assertEquals(4.0, function.getY(1), 1e-9);
    }

    @Test
    public void testInsertAtEnd() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.insert(4.0, 16.0);

        assertEquals(4, function.getCount());
        assertEquals(4.0, function.getX(3), 1e-9);
        assertEquals(16.0, function.getY(3), 1e-9);
        assertEquals(4.0, function.rightBound(), 1e-9);
    }

    @Test
    public void testInsertInMiddle() {
        double[] xValues = {1.0, 3.0, 4.0};
        double[] yValues = {1.0, 9.0, 16.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.insert(2.0, 4.0);

        assertEquals(4, function.getCount());
        assertEquals(2.0, function.getX(1), 1e-9);
        assertEquals(4.0, function.getY(1), 1e-9);
        assertEquals(3.0, function.getX(2), 1e-9);
        assertEquals(9.0, function.getY(2), 1e-9);
    }

    @Test
    public void testInsertReplaceExistingY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.insert(2.0, 8.0); // Заменяем y для существующего x

        assertEquals(3, function.getCount()); // Количество не должно измениться
        assertEquals(2.0, function.getX(1), 1e-9);
        assertEquals(8.0, function.getY(1), 1e-9); // y должен обновиться
    }

    @Test
    public void testInsertMaintainOrder() {
        double[] xValues = {1.0, 5.0};
        double[] yValues = {1.0, 25.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        // Вставляем несколько значений в разном порядке
        function.insert(3.0, 9.0);
        function.insert(2.0, 4.0);
        function.insert(4.0, 16.0);

        assertEquals(5, function.getCount());
        // Проверяем, что порядок сохранился
        assertEquals(1.0, function.getX(0), 1e-9);
        assertEquals(2.0, function.getX(1), 1e-9);
        assertEquals(3.0, function.getX(2), 1e-9);
        assertEquals(4.0, function.getX(3), 1e-9);
        assertEquals(5.0, function.getX(4), 1e-9);

        // Проверяем соответствующие y
        assertEquals(1.0, function.getY(0), 1e-9);
        assertEquals(4.0, function.getY(1), 1e-9);
        assertEquals(9.0, function.getY(2), 1e-9);
        assertEquals(16.0, function.getY(3), 1e-9);
        assertEquals(25.0, function.getY(4), 1e-9);
    }

    @Test
    public void testInsertAndApply() {
        double[] xValues = {1.0, 3.0};
        double[] yValues = {1.0, 9.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.insert(2.0, 4.0);

        // Проверяем, что интерполяция работает корректно после вставки
        assertEquals(2.5, function.apply(1.5), 1e-9);
        assertEquals(4.0, function.apply(2.0), 1e-9);
        assertEquals(6.5, function.apply(2.5), 1e-9);
    }

    @Test
    void testInsertWithEmptyList() { // проверка для случая count = 0
        double[] xValues = {1.0, 2.0}; // создание и удаление
        double[] yValues = {10.0, 20.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        function.remove(0);
        function.remove(0);

        function.insert(5.0, 50.0); // добавление элемента, count = 0

        assertEquals(1, function.getCount());
    }
}