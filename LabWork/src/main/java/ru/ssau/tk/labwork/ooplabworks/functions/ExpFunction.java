package ru.ssau.tk.labwork.ooplabworks.functions;

@UiMathFunction(displayName = "Экспонента", priority = 1)
public class ExpFunction implements MathFunction {
    @Override
    public double apply(double x) {
        return Math.exp(x);
    }
}