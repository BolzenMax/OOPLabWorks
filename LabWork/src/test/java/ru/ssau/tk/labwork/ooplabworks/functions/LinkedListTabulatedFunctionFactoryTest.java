package ru.ssau.tk.labwork.ooplabworks.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.LinkedListTabulatedFunctionFactory;

public class LinkedListTabulatedFunctionFactoryTest {
    @Test
    void create() {
        LinkedListTabulatedFunctionFactory object = new LinkedListTabulatedFunctionFactory();
        Assertions.assertInstanceOf(LinkedListTabulatedFunction.class, object.create(new double[]{1,2,3}, new double[]{1,4,9}));
    }
}
