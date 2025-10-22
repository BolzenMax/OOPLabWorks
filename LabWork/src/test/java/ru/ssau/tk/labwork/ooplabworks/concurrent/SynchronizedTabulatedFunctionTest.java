package ru.ssau.tk.labwork.ooplabworks.concurrent;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.functions.ArrayTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;

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
    public void testIterator() {
    }

}