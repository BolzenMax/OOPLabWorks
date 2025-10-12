package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.ArrayTabulatedFunctionFactory;

public class ArrayTabulatedFunctionFactoryTest {
    @Test
    void create() {
        ArrayTabulatedFunctionFactory object = new ArrayTabulatedFunctionFactory();
        Assertions.assertInstanceOf(ArrayTabulatedFunction.class, object.create(new double[]{1,2,3}, new double[]{1,4,9}));
    }
}