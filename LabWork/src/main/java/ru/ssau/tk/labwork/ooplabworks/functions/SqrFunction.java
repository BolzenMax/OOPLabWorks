package ru.ssau.tk.labwork.ooplabworks.functions;

@UiMathFunction(displayName = "Квадратичная функция", priority = 1)
public class SqrFunction implements MathFunction {
    @Override
    public double apply(double x) {
        return Math.pow(x, 2);
    }
}