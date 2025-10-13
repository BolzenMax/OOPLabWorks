package ru.ssau.tk.labwork.ooplabworks.operations;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.functions.ArrayTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.Point;

import static org.junit.jupiter.api.Assertions.*;

class TabulatedFunctionOperationServiceTest {

    @Test
    void testAsPointsWithArrayTabulatedFunction() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(x, y);

        Point[] points = TabulatedFunctionOperationService.asPoints(f);

        assertEquals(3, points.length);
        assertEquals(1.0, points[0].x, 1e-10);
        assertEquals(10.0, points[0].y, 1e-10);
        assertEquals(2.0, points[1].x, 1e-10);
        assertEquals(20.0, points[1].y, 1e-10);
        assertEquals(3.0, points[2].x, 1e-10);
        assertEquals(30.0, points[2].y, 1e-10);
    }

    @Test
    void testAsPointsWithNull() {
        Point[] points = TabulatedFunctionOperationService.asPoints(null);
        assertEquals(0, points.length);
    }

    @Test
    void testAsPointsMinimal() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(
                new double[]{0.0, 1.0},
                new double[]{0.0, 1.0}
        );
        Point[] points = TabulatedFunctionOperationService.asPoints(f);
        assertEquals(2, points.length);
        assertEquals(0.0, points[0].x, 1e-10);
        assertEquals(0.0, points[0].y, 1e-10);
        assertEquals(1.0, points[1].x, 1e-10);
        assertEquals(1.0, points[1].y, 1e-10);
    }

}