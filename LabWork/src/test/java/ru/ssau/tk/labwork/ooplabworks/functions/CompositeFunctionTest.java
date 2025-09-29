package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CompositeFunctionTest {
    @Test
    void testApply1() { // identity от identity
        MathFunction func = new IdentityFunction();
        CompositeFunction result = new CompositeFunction(func, func);
        Assertions.assertEquals(1, result.apply(1));
        Assertions.assertEquals(-2, result.apply(-2));
        Assertions.assertEquals(0.3, result.apply(0.3));
        Assertions.assertEquals(-0.0001, result.apply(-0.0001));
    }

    @Test
    void testApply2() { // h(x) = g(f(x)) = 4 + 3x, где f(x) = 3x
        MathFunction gFunc = x -> 4 + x;
        MathFunction fFunc = x -> 3 * x;
        CompositeFunction hFunc = new CompositeFunction(gFunc, fFunc);
        Assertions.assertEquals(7, hFunc.apply(1));
        Assertions.assertEquals(16, hFunc.apply(4));
        Assertions.assertEquals(34, hFunc.apply(10));
    }

    @Test
    void testApply3() { // h(x) = g(f(x)) = (x^2)^2, где f(x) = x^2
        SqrFunction gFunc = new SqrFunction();
        SqrFunction fFunc = new SqrFunction();
        CompositeFunction hFunc = new CompositeFunction(gFunc, fFunc);
        Assertions.assertEquals(1, hFunc.apply(1));
        Assertions.assertEquals(16, hFunc.apply(2));
        Assertions.assertEquals(4096, hFunc.apply(8));
    }
}