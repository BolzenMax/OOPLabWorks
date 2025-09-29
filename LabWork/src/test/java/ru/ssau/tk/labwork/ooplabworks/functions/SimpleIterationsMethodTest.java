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

    @Test
    void testMaxIterationsReached() {
        // Тест достижения максимального числа итераций
        MathFunction slowConvergence = (x) -> 0.9 * x + 0.1; // Медленная сходимость
        SimpleIterationsMethod iterationMethod = new SimpleIterationsMethod(slowConvergence, 1e-10, 10); // Мало итераций

        double initial = 100.0;
        double root = iterationMethod.findRoot(initial);

        // Проверяем, что возвращается последнее приближение (не обязательно точное)
        Assertions.assertTrue(Math.abs(root - 1.0) > 1e-3); // Должен быть далек от точного решения
    }

    @Test
    void testApplyMethod() {
        // Тест метода apply (функциональность функции)
        MathFunction squareRootFunction = (x) -> 0.5 * (x + 2 / x);
        SimpleIterationsMethod iterationMethod = new SimpleIterationsMethod(squareRootFunction, 1e-6, 1000);

        // Проверяем, что apply работает как обычная функция
        double result = iterationMethod.apply(2.0);
        double expected = 0.5 * (2.0 + 2 / 2.0); // = 1.5

        Assertions.assertEquals(expected, result, 1e-6);
    }
}