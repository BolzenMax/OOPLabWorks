package ru.ssau.tk.labwork.ooplabworks.concurrent;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.functions.ArrayTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.Point;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.LinkedListTabulatedFunctionFactory;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class SynchronizedTabulatedFunctionTest {
    @Test
    public void testGetCount() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{4, 5, 6});
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(arrayFunction);

        assertEquals(3, syncFunction.getCount());

    }

    @Test
    public void testGetX() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{4, 5, 6});
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(arrayFunction);

        assertEquals(1.0, syncFunction.getX(0));
        assertEquals(2.0, syncFunction.getX(1));
        assertEquals(3.0, syncFunction.getX(2));
    }

    @Test
    public void testGetY() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{4, 5, 6});
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(arrayFunction);

        assertEquals(4.0, syncFunction.getY(0));
        assertEquals(5.0, syncFunction.getY(1));
        assertEquals(6.0, syncFunction.getY(2));
    }

    @Test
    public void testSetY() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{4, 5, 6});
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(arrayFunction);

        syncFunction.setY(1, 25.0);
        assertEquals(25.0, syncFunction.getY(1));

        syncFunction.setY(0, 15.0);
        assertEquals(15.0, syncFunction.getY(0));
    }

    @Test
    public void testIndexOfX() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{4, 5, 6});
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(arrayFunction);

        assertEquals(0, syncFunction.indexOfX(1));
        assertEquals(1, syncFunction.indexOfX(2));
        assertEquals(2, syncFunction.indexOfX(3));
        assertEquals(-1, syncFunction.indexOfX(69.1488));
    }

    @Test
    public void testIndexOfY() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{4, 5, 6});
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(arrayFunction);

        assertEquals(0, syncFunction.indexOfY(4));
        assertEquals(1, syncFunction.indexOfY(5));
        assertEquals(2, syncFunction.indexOfY(6));
    }

    @Test
    public void testLeftBound() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{4, 5, 6});
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(arrayFunction);

        assertEquals(1, syncFunction.leftBound());
    }

    @Test
    public void testRightBound() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{4, 5, 6});
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(arrayFunction);

        assertEquals(3, syncFunction.rightBound());
    }

    @Test
    public void testApply() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(new double[]{0, 2, 4}, new double[]{0, 4, 16});
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(arrayFunction);


        assertEquals(0.0, syncFunction.apply(0));
        assertEquals(4.0, syncFunction.apply(2));
        assertEquals(16.0, syncFunction.apply(4));


        assertEquals(2.0, syncFunction.apply(1));
        assertEquals(10.0, syncFunction.apply(3));
    }

    @Test
    public void testDoSynchronouslyWithReturnValue() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(
                new double[]{1, 2, 3, 4},
                new double[]{10, 20, 30, 40}
        );
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(arrayFunction);


        Double result = syncFunction.doSynchronously(function -> {
            double sum = 0;
            int count = function.getCount();
            for (int i = 0; i < count; i++) {
                sum += function.getY(i);
            }
            return sum / count;
        });

        assertEquals(25.0, result); // (10+20+30+40)/4 = 25
    }

    @Test
    public void testDoSynchronouslyWithVoid() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 2, 3}
        );
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(arrayFunction);


        Void result = syncFunction.doSynchronously(function -> {
            for (int i = 0; i < function.getCount(); i++) {
                function.setY(i, function.getY(i) * 2);
            }
            return null;
        });

        assertNull(result);


        assertEquals(2.0, syncFunction.getY(0));
        assertEquals(4.0, syncFunction.getY(1));
        assertEquals(6.0, syncFunction.getY(2));
    }
    @Test
    public void testDoSynchronouslyWithBooleanResult() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(
                new double[]{1, 2, 3},
                new double[]{1, 2, 3}
        );
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(arrayFunction);

        Boolean isMonotonic = syncFunction.doSynchronously(function -> {
            for (int i = 0; i < function.getCount() - 1; i++) {
                if (function.getY(i) >= function.getY(i + 1)) {
                    return false;
                }
            }
            return true;
        });

        assertTrue(isMonotonic);
    }

    @Test
    void testIteratorHasNextAndNext() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunctionFactory().create(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        Iterator<Point> iterator = syncFunction.iterator();

        assertTrue(iterator.hasNext()); // проверка наличия

        Point point1 = iterator.next(); // проверка последовательного чтения всех точек
        assertEquals(1.0, point1.x, 1e-9);
        assertEquals(10.0, point1.y, 1e-9);

        Point point2 = iterator.next();
        assertEquals(2.0, point2.x, 1e-9);
        assertEquals(20.0, point2.y, 1e-9);

        Point point3 = iterator.next();
        assertEquals(3.0, point3.x, 1e-9);
        assertEquals(30.0, point3.y, 1e-9);

        Point point4 = iterator.next();
        assertEquals(4.0, point4.x, 1e-9);
        assertEquals(40.0, point4.y, 1e-9);

        assertFalse(iterator.hasNext()); // проверка того, что после прохождения hasNext идёт в false
    }

    @Test
    void testIteratorNoSuchElementException() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};
        TabulatedFunction baseFunction = new LinkedListTabulatedFunctionFactory().create(xValues, yValues);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        Iterator<Point> iterator = syncFunction.iterator();

        while (iterator.hasNext()) {
            iterator.next();
        }
        assertThrows(NoSuchElementException.class, iterator::next);
    }
}