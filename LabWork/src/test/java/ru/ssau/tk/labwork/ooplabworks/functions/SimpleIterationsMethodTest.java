package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class SimpleIterationsMethodTest {
    @Test
    void testFindRoot() { // сх. к корень кв. из 3
        MathFunction exampleFunction = (x) -> 0.5 * (x + 3 / x);
        SimpleIterationsMethod iterationMethod = new SimpleIterationsMethod(exampleFunction, 1e-6, 1000);

        double initial = 1.0;
        double root = iterationMethod.findRoot(initial);

        Assertions.assertEquals(Math.sqrt(3), root, 1e-6); // корень совпадает с ожидаемым результатом с точностью 1e-6
    }

    @Test
    void testFindNegativeRoot() { // сх. к -корень кв. из 3
        MathFunction exampleFunction = (x) -> 0.5 * (x + 3 / x);
        SimpleIterationsMethod iterationMethod = new SimpleIterationsMethod(exampleFunction, 1e-6, 1000);

        double initial = -1.0;
        double root = iterationMethod.findRoot(initial);

        Assertions.assertEquals(-Math.sqrt(3), root, 1e-6); // корень совпадает с ожидаемым результатом с точностью 1e-6
    }

    @Test
    void testFindRootZero() { // сходимость к 0
        MathFunction testFindRootZero = (x) -> 0.1 * x;
        SimpleIterationsMethod iterationMethod = new SimpleIterationsMethod(testFindRootZero, 1e-6, 1000);

        double initial = 100.0;
        double root = iterationMethod.findRoot(initial);

        Assertions.assertEquals(0, root, 1e-6);
    }

    @Test
    void testFindRootWithMaxIterations() { // сх. к 0.99
        MathFunction slowConvergence = (x) -> 0.99 * x + 0.01;
        SimpleIterationsMethod iterationMethod = new SimpleIterationsMethod(slowConvergence, 1e-6, 50);

        double initial = 100.0;
        double root = iterationMethod.findRoot(initial);

        Assertions.assertTrue(Math.abs(root - 1.0) > 1e-3); // возвращение значения в т. ч. если не достигнута точность
    }

    @Test
    void testApplyMethod() { // проверка apply
        MathFunction squareRootFunction = (x) -> 0.5 * (x + 2 / x);
        SimpleIterationsMethod iterationMethod = new SimpleIterationsMethod(squareRootFunction, 1e-6, 1000);

        double result = iterationMethod.apply(2.0);
        double expected = 0.5 * (2.0 + 2 / 2.0);

        Assertions.assertEquals(expected, result, 1e-6);
    }
}