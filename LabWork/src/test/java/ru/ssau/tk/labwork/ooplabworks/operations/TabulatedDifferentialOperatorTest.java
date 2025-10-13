package ru.ssau.tk.labwork.ooplabworks.operations;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.LinkedListTabulatedFunctionFactory;

import static org.junit.jupiter.api.Assertions.*;

public class TabulatedDifferentialOperatorTest {
    @Test
    void testDeriveWithArrayFactory() {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(new ArrayTabulatedFunctionFactory());

        double[] xValues = {0.0, 1.0, 2.0, 3.0, 4.0}; // f(x) = x^2, f'(x) = 2x
        double[] yValues = {0.0, 1.0, 4.0, 9.0, 16.0};

        TabulatedFunction function = operator.getFactory().create(xValues, yValues);
        TabulatedFunction derivative = operator.derive(function);

        assertEquals(1.0, derivative.getY(0), 1e-9);
        assertEquals(3.0, derivative.getY(1), 1e-9);
        assertEquals(5.0, derivative.getY(2), 1e-9);
        assertEquals(7.0, derivative.getY(3), 1e-9);
        assertEquals(7.0, derivative.getY(4), 1e-9);
    }

    @Test
    void testDeriveWithLinkedListFactory() {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());

        double[] xValues = {0.0, 1.0, 2.0, 3.0, 4.0};
        double[] yValues = {0.0, 1.0, 4.0, 9.0, 16.0};

        TabulatedFunction function = operator.getFactory().create(xValues, yValues);
        TabulatedFunction derivative = operator.derive(function);

        assertEquals(1.0, derivative.getY(0), 1e-9);
        assertEquals(3.0, derivative.getY(1), 1e-9);
        assertEquals(5.0, derivative.getY(2), 1e-9);
        assertEquals(7.0, derivative.getY(3), 1e-9);
        assertEquals(7.0, derivative.getY(4), 1e-9);
    }

    @Test
    void testFactoryGetterAndSetter() {
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
        assertNotNull(operator.getFactory());

        LinkedListTabulatedFunctionFactory newFactory = new LinkedListTabulatedFunctionFactory();
        operator.setFactory(newFactory);
    }
}