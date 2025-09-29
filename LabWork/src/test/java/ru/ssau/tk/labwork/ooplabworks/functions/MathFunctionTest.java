package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MathFunctionTest {

    @Test
    void testThreeElementChain() {
        MathFunction identity = new IdentityFunction();
        MathFunction sqr = new SqrFunction();
        MathFunction unit = new UnitFunction();

        // identity -> sqr -> unit
        MathFunction chain = identity.andThen(sqr).andThen(unit);

        assertEquals(1.0, chain.apply(0.0), 1e-9);
        assertEquals(1.0, chain.apply(5.0), 1e-9);
        assertEquals(1.0, chain.apply(-3.0), 1e-9);
    }

    @Test
    void testChainWithZeroFunction() {
        MathFunction zero = new ZeroFunction();
        MathFunction sqr = new SqrFunction();
        MathFunction identity = new IdentityFunction();

        // zero -> sqr -> identity
        MathFunction chain = zero.andThen(sqr).andThen(identity);

        assertEquals(0.0, chain.apply(100.0), 1e-9);
        assertEquals(0.0, chain.apply(-50.0), 1e-9);
    }

    @Test
    void testMixedChain() {
        MathFunction constant = new ConstantFunction(2.0);
        MathFunction identity = new IdentityFunction();
        MathFunction zero = new ZeroFunction();

        // constant -> identity -> zero
        MathFunction chain = identity.andThen(zero).andThen(constant);

        assertEquals(0.0, chain.apply(999.0), 1e-9);
        assertEquals(0.0, chain.apply(-123.0), 1e-9);
    }

    @Test
    void testComplexChain() {
        MathFunction unit = new UnitFunction();
        MathFunction zero = new ZeroFunction();
        MathFunction sqr = new SqrFunction();

        // unit -> zero -> sqr
        MathFunction chain = unit.andThen(zero).andThen(sqr);

        assertEquals(1.0, chain.apply(42.0), 1e-9);
        assertEquals(1.0, chain.apply(-7.0), 1e-9);
        assertEquals(1.0, chain.apply(-1212.0), 1e-9);
    }
}