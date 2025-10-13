package ru.ssau.tk.labwork.ooplabworks.operations;

import ru.ssau.tk.labwork.ooplabworks.functions.MathFunction;

public class LeftSteppingDifferentialOperator extends SteppingDifferentialOperator {

    public LeftSteppingDifferentialOperator(double step) {
        super(step);
    }

    @Override
    public MathFunction derive(MathFunction function) {
        return new MathFunction() {
            @Override
            public double apply(double x) {
                // (f(x) - f(x - step)) / step
                return (function.apply(x) - function.apply(x - step)) / step;
            }
        };
    }
}