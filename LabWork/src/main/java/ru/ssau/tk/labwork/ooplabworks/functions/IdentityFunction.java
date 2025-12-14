package ru.ssau.tk.labwork.ooplabworks.functions;

@UiMathFunction(displayName = "Тождественная функция", priority = 1)
public class IdentityFunction implements MathFunction {
    public double apply(double x) {
        return x;
    }
}