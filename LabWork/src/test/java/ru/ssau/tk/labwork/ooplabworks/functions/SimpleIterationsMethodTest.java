package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class SimpleIterationsMethodTest {
    @Test
    void testFindRoot() {
        MathFunction exampleFunction = (x) -> 0.5 * (x + 3 / x);
        SimpleIterationsMethod iterationMethod = new SimpleIterationsMethod(exampleFunction, 1e-6, 1000);

        double initial = 1.0;
        double root = iterationMethod.findRoot(initial);

        Assertions.assertEquals(Math.sqrt(3), root, 1e-6); //корень совпадает с ожидаемым результатом с точностью 1e-6
    }

    @Test
    void testFindRootZero() { // функция, которая быстро сходится
        MathFunction testFindRootZero = (x) -> 0.1 * x;
        SimpleIterationsMethod iterationMethod = new SimpleIterationsMethod(testFindRootZero, 1e-6, 1000);

        double initial = 100.0;
        double root = iterationMethod.findRoot(initial);

        Assertions.assertEquals(0, root, 1e-6);
    }
}