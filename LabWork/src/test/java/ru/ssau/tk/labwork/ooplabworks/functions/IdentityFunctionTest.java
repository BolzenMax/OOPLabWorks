package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IdentityFunctionTest {
    private IdentityFunction function;

    @BeforeEach
    void setUp() {
        function = new IdentityFunction();
    }

    @Test
    void applyPositiveInteger() {
        double res = function.apply(5);
        double res1 = function.apply(7);
        double res2 = function.apply(17);
        Assertions.assertEquals(5, res);
        Assertions.assertEquals(7, res1);
        Assertions.assertEquals(17, res2);
    }

    @Test
    void applyNegativeInteger() {
        double res = function.apply(-2);
        double res1 = function.apply(-10);
        double res2 = function.apply(-20);
        Assertions.assertEquals(-2, res);
        Assertions.assertEquals(-10, res1);
        Assertions.assertEquals(-20, res2);
    }

    @Test
    void applyPositiveDouble() {
        double res = function.apply(0.3);
        double res1 = function.apply(0.0001);
        double res2 = function.apply(3.1415926);
        Assertions.assertEquals(0.3, res);
        Assertions.assertEquals(0.0001, res1);
        Assertions.assertEquals(3.1415926, res2);
    }

    @Test
    void applyNegativeDouble() {
        double res = function.apply(-0.0001);
        double res1 = function.apply(-0.17);
        double res2 = function.apply(-2.718);
        Assertions.assertEquals(-0.0001, res);
        Assertions.assertEquals(-0.17, res1);
        Assertions.assertEquals(-2.718, res2);
    }
}