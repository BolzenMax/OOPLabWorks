package ru.ssau.tk.labwork.ooplabworks.functions;

@UiMathFunction(displayName = "Синус", priority = 1)
public class SinFunction implements MathFunction {
    @Override
    public double apply(double x) {
        return Math.sin(x);
    }
}