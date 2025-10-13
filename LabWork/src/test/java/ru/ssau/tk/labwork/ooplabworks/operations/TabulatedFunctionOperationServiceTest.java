package ru.ssau.tk.labwork.ooplabworks.operations;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.labwork.ooplabworks.functions.ArrayTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.Point;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.TabulatedFunctionFactory;

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

    @Test
    void testAddWithArrayFactory() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(new ArrayTabulatedFunctionFactory());
        TabulatedFunction f1 = new ArrayTabulatedFunction(new double[]{1, 2}, new double[]{10, 20});
        TabulatedFunction f2 = new ArrayTabulatedFunction(new double[]{1, 2}, new double[]{1, 2});

        TabulatedFunction result = service.add(f1, f2);
        assertEquals(11, result.getY(0), 1e-10);
        assertEquals(22, result.getY(1), 1e-10);

    }

    @Test
    void testSubtractWithLinkedListFactory() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction f1 = new LinkedListTabulatedFunction(new double[]{1, 2}, new double[]{10, 20});
        TabulatedFunction f2 = new LinkedListTabulatedFunction(new double[]{1, 2}, new double[]{3, 4});

        TabulatedFunction result = service.subtract(f1, f2);
        assertEquals(7, result.getY(0), 1e-10);
        assertEquals(16, result.getY(1), 1e-10);

    }

    @Test
    void testAddDifferentTypes() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(new ArrayTabulatedFunctionFactory());
        TabulatedFunction f1 = new ArrayTabulatedFunction(new double[]{1, 2}, new double[]{10, 20});
        TabulatedFunction f2 = new LinkedListTabulatedFunction(new double[]{1, 2}, new double[]{1, 2});

        TabulatedFunction result = service.add(f1, f2);
        assertEquals(11, result.getY(0), 1e-10);
        assertEquals(22, result.getY(1), 1e-10);

    }

    @Test
    void testSubtractDifferentTypes() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction f1 = new LinkedListTabulatedFunction(new double[]{1, 2}, new double[]{10, 20});
        TabulatedFunction f2 = new ArrayTabulatedFunction(new double[]{1, 2}, new double[]{3, 4});

        TabulatedFunction result = service.subtract(f1, f2);
        assertEquals(7, result.getY(0), 1e-10);
        assertEquals(16, result.getY(1), 1e-10);

    }
    @Test
    void testMultiplicationDifferentTypes() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(new ArrayTabulatedFunctionFactory());
        TabulatedFunction f1 = new ArrayTabulatedFunction(new double[]{1, 2}, new double[]{3, 4});
        TabulatedFunction f2 = new LinkedListTabulatedFunction(new double[]{1, 2}, new double[]{5, 6});

        TabulatedFunction result = service.multiplication(f1, f2);
        assertEquals(15, result.getY(0), 1e-10);
        assertEquals(24, result.getY(1), 1e-10);
    }

    @Test
    void testDivisionDifferentTypes() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction f1 = new LinkedListTabulatedFunction(new double[]{2, 4}, new double[]{10, 20});
        TabulatedFunction f2 = new ArrayTabulatedFunction(new double[]{2, 4}, new double[]{2, 5});

        TabulatedFunction result = service.division(f1, f2);
        assertEquals(5, result.getY(0), 1e-10);
        assertEquals(4, result.getY(1), 1e-10);

    }
    @Test
    void testDifferentCountsThrowsException() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        TabulatedFunction a = new ArrayTabulatedFunction(new double[]{1, 2}, new double[]{10, 20});
        TabulatedFunction b = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{1, 2, 3});

        assertThrows(InconsistentFunctionsException.class, () -> service.add(a, b));
    }

    @Test
    void testGetAndSetFactory() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        TabulatedFunctionFactory newFactory = new LinkedListTabulatedFunctionFactory();
        service.setFactory(newFactory);
        assertSame(newFactory, service.getFactory());
    }

    @Test
    void testInconsistentXValuesThrowException() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        TabulatedFunction a = new ArrayTabulatedFunction(new double[]{1.0, 2.0}, new double[]{10, 20});
        TabulatedFunction b = new ArrayTabulatedFunction(new double[]{1.0, 3}, new double[]{1, 2});

        assertThrows(InconsistentFunctionsException.class, () -> service.add(a, b));
    }

    @Test
    void testDefaultConstructorUsesArrayFactory() {
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

    }
}