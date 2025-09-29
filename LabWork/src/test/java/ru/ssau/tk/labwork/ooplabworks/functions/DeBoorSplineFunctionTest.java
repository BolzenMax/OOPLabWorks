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
    @Test
    void testFindKnotSpanAtLastKnot() {
        double[] knots = {0, 0, 0, 1, 1, 1};
        double[][] controlPoints = {{0, 0}, {1, 2}, {3, 1}};

        // Тестируем случай, когда t равен последнему узлу
        double[] result = DeBoorSplineFunction.deBoor(2, knots, controlPoints, 1.0);
        assertArrayEquals(controlPoints[2], result, 1e-10);
    }

    @Test
    void testFindKnotSpanGreaterThanLastKnot() {
        double[] knots = {0, 0, 0, 1, 1, 1};
        double[][] controlPoints = {{0, 0}, {1, 2}, {3, 1}};

        // Тестируем случай, когда t больше последнего узла
        // В этом случае алгоритм Де Бура экстраполирует кривую за пределы узлового вектора
        double[] result = DeBoorSplineFunction.deBoor(2, knots, controlPoints, 1.5);

        // Проверяем что результат корректен (не NaN, не бесконечность)
        assertNotNull(result);
        assertEquals(2, result.length);
        assertTrue(Double.isFinite(result[0]));
        assertTrue(Double.isFinite(result[1]));

        // Для кубического B-сплайна при t > последнего узла результат может быть экстраполирован
        // Вместо проверки точного значения проверяем что это разумная экстраполяция
        assertTrue(result[0] >= controlPoints[2][0]); // x должен быть >= последней контрольной точки
    }


    @Test
    void testFindKnotSpanBinarySearchRight() {
        // Создаем узловой вектор, где потребуется бинарный поиск в правой части
        double[] knots = {0, 1, 2, 3, 4, 5, 6, 7};
        double[][] controlPoints = {{0, 0}, {1, 1}, {2, 2}, {3, 3}, {4, 4}};

        double[] result = DeBoorSplineFunction.deBoor(2, knots, controlPoints, 5.5);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test
    void testFindKnotSpanExactKnotValue() {
        // Тестируем случай, когда t точно совпадает с узлом (не последним)
        double[] knots = {0, 1, 2, 3, 4, 5, 6, 7};
        double[][] controlPoints = {{0, 0}, {1, 1}, {2, 2}, {3, 3}, {4, 4}};

        double[] result = DeBoorSplineFunction.deBoor(2, knots, controlPoints, 3.0);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test
    void testAlgorithmAllIterations() {
        // Тест, который гарантированно пройдет через все итерации алгоритма Де Бура
        double[] knots = {0, 0, 0, 0, 1, 2, 3, 3, 3, 3};
        double[][] controlPoints = {
                {0, 0}, {1, 2}, {3, 1}, {4, 3}, {5, 2}, {6, 0}
        };

        // Тестируем в середине кривой, где задействованы все степени
        double[] result = DeBoorSplineFunction.deBoor(3, knots, controlPoints, 1.5);
        assertNotNull(result);
        assertEquals(2, result.length);

        // Дополнительные точки для полного покрытия
        double[] result2 = DeBoorSplineFunction.deBoor(3, knots, controlPoints, 0.5);
        double[] result3 = DeBoorSplineFunction.deBoor(3, knots, controlPoints, 2.5);
        assertNotNull(result2);
        assertNotNull(result3);
    }

    @Test
    void testEdgeCaseMinimumValidInput() {
        // Минимально возможные валидные входные данные
        double[] knots = {0, 1};
        double[][] controlPoints = {{0, 0}};

        double[] result = DeBoorSplineFunction.deBoor(0, knots, controlPoints, 0.5);
        assertArrayEquals(controlPoints[0], result, 1e-10);
    }

    @Test
    void testDifferentDimensions() {
        // Тестируем точки разной размерности
        double[] knots = {0, 0, 1, 1};
        double[][] controlPoints1D = {{0}, {1}};
        double[][] controlPoints4D = {{0, 0, 0, 0}, {1, 1, 1, 1}};

        double[] result1D = DeBoorSplineFunction.deBoor(1, knots, controlPoints1D, 0.5);
        double[] result4D = DeBoorSplineFunction.deBoor(1, knots, controlPoints4D, 0.5);

        assertArrayEquals(new double[]{0.5}, result1D, 1e-10);
        assertArrayEquals(new double[]{0.5, 0.5, 0.5, 0.5}, result4D, 1e-10);
    }
}