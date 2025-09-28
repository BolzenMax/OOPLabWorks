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
        Assertions.assertEquals(5,res);
    }

    @Test
    void applyNegativeInteger() {
        double res = function.apply(-2);
        Assertions.assertEquals(-2,res);
    }

    @Test
    void applyPositiveDouble() {
        double res = function.apply(0.3);
        Assertions.assertEquals(0.3,res);
    }

    @Test
    void applyNegativeDouble() {
        double res = function.apply(-0.0001);
        Assertions.assertEquals(-0.0001,res);
    }
}