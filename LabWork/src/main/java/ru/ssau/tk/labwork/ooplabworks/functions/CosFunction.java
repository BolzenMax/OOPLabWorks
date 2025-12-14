package ru.ssau.tk.labwork.ooplabworks.functions;

@UiMathFunction(displayName = "Косинус", priority = 1)
public class CosFunction implements MathFunction {
    @Override
    public double apply(double x) {
        return Math.cos(x);
    }
}