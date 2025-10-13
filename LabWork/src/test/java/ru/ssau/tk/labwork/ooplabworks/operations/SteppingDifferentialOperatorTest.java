package ru.ssau.tk.labwork.ooplabworks.operations;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.labwork.ooplabworks.functions.ConstantFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.MathFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.SqrFunction;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SteppingDifferentialOperatorTest {

    @Test
    void testLeftDerivativeOfSqrFunction() {
        LeftSteppingDifferentialOperator op = new LeftSteppingDifferentialOperator(0.1);
        SqrFunction sqr = new SqrFunction();
        MathFunction derivative = op.derive(sqr);

        assertEquals(2 * 1.0 - 0.1, derivative.apply(1.0), 1e-10);
        assertEquals(2 * 2.0 - 0.1, derivative.apply(2.0), 1e-10);
    }

    @Test
    void testRightDerivativeOfSqrFunction() {
        RightSteppingDifferentialOperator op = new RightSteppingDifferentialOperator(0.1);
        SqrFunction sqr = new SqrFunction();
        MathFunction derivative = op.derive(sqr);

        assertEquals(2 * 1.0 + 0.1, derivative.apply(1.0), 1e-10);
        assertEquals(2 * 2.0 + 0.1, derivative.apply(2.0), 1e-10);
    }

    @Test
    void testLeftDerivativeOfConstantFunction() {
        LeftSteppingDifferentialOperator op = new LeftSteppingDifferentialOperator(0.5);
        ConstantFunction constFunc = new ConstantFunction(5.0);
        MathFunction derivative = op.derive(constFunc);

        assertEquals(0.0, derivative.apply(10.0), 1e-10);
        assertEquals(0.0, derivative.apply(-3.0), 1e-10);
    }

    @Test
    void testRightDerivativeOfConstantFunction() {
        RightSteppingDifferentialOperator op = new RightSteppingDifferentialOperator(0.5);
        ConstantFunction constFunc = new ConstantFunction(5.0);
        MathFunction derivative = op.derive(constFunc);

        assertEquals(0.0, derivative.apply(10.0), 1e-10);
        assertEquals(0.0, derivative.apply(-3.0), 1e-10);
    }

}