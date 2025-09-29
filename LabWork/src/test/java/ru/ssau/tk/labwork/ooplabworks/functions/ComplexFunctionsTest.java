package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComplexFunctionsTest {

    @Test
    public void testTabulatedFunctionWithCompositeFunction() {
        // Создаем табулированную функцию из массива
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction tabulatedFunc = new ArrayTabulatedFunction(xValues, yValues);

        // Создаем композитную функцию: sqr(tabulatedFunc(x))
        MathFunction composite = new CompositeFunction(new SqrFunction(), tabulatedFunc);

        // Проверяем композитную функцию
        assertEquals(1.0, composite.apply(1.0));   // sqr(1.0) = 1.0
        assertEquals(16.0, composite.apply(2.0));  // sqr(4.0) = 16.0
        assertEquals(81.0, composite.apply(3.0));  // sqr(9.0) = 81.0
    }

    @Test
    public void testCompositeFunctionWithTabulatedFunction() {
        // Создаем табулированную функцию из массива
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {0.0, 2.0, 4.0};
        ArrayTabulatedFunction tabulatedFunc = new ArrayTabulatedFunction(xValues, yValues);

        // Создаем композитную функцию: tabulatedFunc(sqr(x))
        MathFunction composite = new CompositeFunction(tabulatedFunc, new SqrFunction());

        // Проверяем композитную функцию
        assertEquals(0.0, composite.apply(0.0));   // tabulatedFunc(0.0) = 0.0
        assertEquals(2.0, composite.apply(1.0));   // tabulatedFunc(1.0) = 2.0

    }

    @Test
    public void testTabulatedFunctionWithAndThen() {
        // Создаем табулированную функцию из массива
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {2.0, 4.0, 6.0};
        ArrayTabulatedFunction tabulatedFunc = new ArrayTabulatedFunction(xValues, yValues);

        // Используем метод andThen для создания цепочки функций
        MathFunction chain = new SqrFunction().andThen(tabulatedFunc);

        // Проверяем цепочку: sqr(tabulatedFunc(x))
        assertEquals(4.0, chain.apply(1.0));   // sqr(2.0) = 4.0
        assertEquals(16.0, chain.apply(2.0));  // sqr(4.0) = 16.0
        assertEquals(36.0, chain.apply(3.0));  // sqr(6.0) = 36.0
    }

    @Test
    public void testMultipleAndThenWithTabulatedFunction() {
        // Создаем табулированную функцию из массива
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {1.0, 3.0, 5.0};
        ArrayTabulatedFunction tabulatedFunc = new ArrayTabulatedFunction(xValues, yValues);

        // Создаем сложную цепочку: constant -> tabulatedFunc -> sqr
        MathFunction constant = new ConstantFunction(1.0);
        MathFunction complexChain = new SqrFunction().andThen(tabulatedFunc).andThen(constant);

        // Проверяем: sqr(tabulatedFunc(constant(x)))
        // constant всегда возвращает 1.0, tabulatedFunc(1.0) = 3.0, sqr(3.0) = 9.0
        assertEquals(9.0, complexChain.apply(100.0)); // Любой x даст одинаковый результат
    }

    @Test
    public void testTabulatedFunctionWithIdentityFunction() {
        // Создаем табулированную функцию из массива
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction tabulatedFunc = new ArrayTabulatedFunction(xValues, yValues);

        // Композит: identity(tabulatedFunc(x)) - должен вернуть то же значение
        MathFunction composite = new CompositeFunction(new IdentityFunction(), tabulatedFunc);

        assertEquals(10.0, composite.apply(1.0));
        assertEquals(20.0, composite.apply(2.0));
        assertEquals(30.0, composite.apply(3.0));
    }

    @Test
    public void testTabulatedFunctionWithConstantFunction() {
        // Создаем табулированную функцию из массива
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {5.0, 10.0, 15.0};
        ArrayTabulatedFunction tabulatedFunc = new ArrayTabulatedFunction(xValues, yValues);

        // Композит: constant(tabulatedFunc(x)) - всегда возвращает константу
        MathFunction constant = new ConstantFunction(100.0);
        MathFunction composite = new CompositeFunction(constant, tabulatedFunc);

        // Независимо от x, всегда возвращает 100.0
        assertEquals(100.0, composite.apply(1.0));
        assertEquals(100.0, composite.apply(2.0));
        assertEquals(100.0, composite.apply(3.0));
    }

    @Test
    public void testInterpolatedTabulatedFunctionInComposite() {
        // Создаем табулированную функцию, которая будет использовать интерполяцию
        double[] xValues = {0.0, 2.0, 4.0};
        double[] yValues = {0.0, 4.0, 16.0};
        ArrayTabulatedFunction tabulatedFunc = new ArrayTabulatedFunction(xValues, yValues);

        // Композит: sqr(tabulatedFunc(x))
        MathFunction composite = new CompositeFunction(new SqrFunction(), tabulatedFunc);

        // Проверяем интерполированные значения
        double result1 = composite.apply(1.0); // tabulatedFunc(1.0) = 2.0 (интерполяция), sqr(2.0) = 4.0
        double result2 = composite.apply(3.0); // tabulatedFunc(3.0) = 10.0 (интерполяция), sqr(10.0) = 100.0

        assertTrue(result1 > 0.0);
        assertTrue(result2 > 0.0);
        assertEquals(16.0, composite.apply(2.0)); // Точное значение
    }

    @Test
    public void testChainedTabulatedFunctions() {
        // Первая табулированная функция
        double[] xValues1 = {1.0, 2.0, 3.0};
        double[] yValues1 = {2.0, 4.0, 6.0};
        ArrayTabulatedFunction func1 = new ArrayTabulatedFunction(xValues1, yValues1);

        // Вторая табулированная функция
        double[] xValues2 = {2.0, 4.0, 6.0};
        double[] yValues2 = {4.0, 16.0, 36.0};
        ArrayTabulatedFunction func2 = new ArrayTabulatedFunction(xValues2, yValues2);

        // Цепочка: func2(func1(x))
        MathFunction chain = new CompositeFunction(func2, func1);

        assertEquals(4.0, chain.apply(1.0));   // func1(1.0)=2.0, func2(2.0)=4.0
        assertEquals(16.0, chain.apply(2.0));  // func1(2.0)=4.0, func2(4.0)=16.0
        assertEquals(36.0, chain.apply(3.0));  // func1(3.0)=6.0, func2(6.0)=36.0
    }
}