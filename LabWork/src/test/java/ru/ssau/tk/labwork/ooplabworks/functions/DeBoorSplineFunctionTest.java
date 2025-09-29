package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeBoorSplineFunctionTest {

    @Test
    void shouldThrowExceptionWhenKnotsNull() {
        double[][] controlPoints = {{0, 0}, {1, 1}};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> DeBoorSplineFunction.deBoor(1, null, controlPoints, 0.5));
        assertEquals("Knots and control points cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenControlPointsNull() {
        double[] knots = {0, 0, 1, 1};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> DeBoorSplineFunction.deBoor(1, knots, null, 0.5));
        assertEquals("Knots and control points cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNegativeDegree() {
        double[] knots = {0, 0, 1, 1};
        double[][] controlPoints = {{0, 0}, {1, 1}};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> DeBoorSplineFunction.deBoor(-1, knots, controlPoints, 0.5));
        assertEquals("Degree must be non-negative", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenInvalidKnotsLength() {
        double[] knots = {0, 0, 1}; // Неправильная длина
        double[][] controlPoints = {{0, 0}, {1, 1}, {2, 2}};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> DeBoorSplineFunction.deBoor(2, knots, controlPoints, 0.5));
        assertTrue(exception.getMessage().contains("Invalid number of knots"));
    }

    @Test
    void testLinearSpline() {
        double[] knots = {0, 0, 1, 1};
        double[][] controlPoints = {{0, 0}, {1, 1}};

        double[] result = DeBoorSplineFunction.deBoor(1, knots, controlPoints, 0.5);
        assertArrayEquals(new double[]{0.5, 0.5}, result, 1e-10);
    }

    @Test
    void testQuadraticSpline() {
        double[] knots = {0, 0, 0, 1, 1, 1};
        double[][] controlPoints = {{0, 0}, {1, 2}, {3, 1}};

        double[] result = DeBoorSplineFunction.deBoor(2, knots, controlPoints, 0.5);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test
    void testCubicSpline() {
        double[] knots = {0, 0, 0, 0, 1, 1, 1, 1};
        double[][] controlPoints = {{0, 0}, {1, 1}, {2, 3}, {3, 2}};

        double[] result = DeBoorSplineFunction.deBoor(3, knots, controlPoints, 0.5);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test
    void testCubicSplineHelperMethod() {
        double[] knots = {0, 0, 0, 0, 1, 1, 1, 1};
        double[][] controlPoints = {{0, 0}, {1, 1}, {2, 3}, {3, 2}};

        double[] result1 = DeBoorSplineFunction.deBoorCubic(knots, controlPoints, 0.5);
        double[] result2 = DeBoorSplineFunction.deBoor(3, knots, controlPoints, 0.5);

        assertArrayEquals(result2, result1, 1e-10);
    }

    @Test
    void testAtStartPoint() {
        double[] knots = {0, 0, 0, 1, 1, 1};
        double[][] controlPoints = {{0, 0}, {1, 2}, {3, 1}};

        double[] result = DeBoorSplineFunction.deBoor(2, knots, controlPoints, 0.0);
        assertArrayEquals(controlPoints[0], result, 1e-10);
    }

    @Test
    void testAtEndPoint() {
        double[] knots = {0, 0, 0, 1, 1, 1};
        double[][] controlPoints = {{0, 0}, {1, 2}, {3, 1}};

        double[] result = DeBoorSplineFunction.deBoor(2, knots, controlPoints, 1.0);
        assertArrayEquals(controlPoints[2], result, 1e-10);
    }

    @Test
    void testThreeDimensionalPoints() {
        double[] knots = {0, 0, 1, 1};
        double[][] controlPoints = {{0, 0, 0}, {1, 1, 1}};

        double[] result = DeBoorSplineFunction.deBoor(1, knots, controlPoints, 0.5);
        assertArrayEquals(new double[]{0.5, 0.5, 0.5}, result, 1e-10);
    }



    @Test
    void testMultipleKnotSpans() {
        double[] knots = {0, 0, 0, 1, 2, 2, 2};
        double[][] controlPoints = {{0, 0}, {1, 1}, {2, 3}, {3, 2}};

        double[] result = DeBoorSplineFunction.deBoor(2, knots, controlPoints, 1.5);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test
    void testPrecision() {
        double[] knots = {0, 0, 1, 1};
        double[][] controlPoints = {{0, 0}, {1, 1}};

        double[] result1 = DeBoorSplineFunction.deBoor(1, knots, controlPoints, 0.1);
        double[] result2 = DeBoorSplineFunction.deBoor(1, knots, controlPoints, 0.9);

        assertArrayEquals(new double[]{0.1, 0.1}, result1, 1e-10);
        assertArrayEquals(new double[]{0.9, 0.9}, result2, 1e-10);
    }
}