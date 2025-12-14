package ru.ssau.tk.labwork.ooplabworks.functions;

@UiMathFunction(displayName = "Натуральный логарифм ln(x)", priority = 1)
public class LnFunction implements MathFunction {
    @Override
    public double apply(double x) {
        return Math.log(x);
    }
}