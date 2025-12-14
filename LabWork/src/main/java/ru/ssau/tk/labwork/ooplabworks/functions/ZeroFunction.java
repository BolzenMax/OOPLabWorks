package ru.ssau.tk.labwork.ooplabworks.functions;

@UiMathFunction(displayName = "Нулевая функция", priority = 2)
public class ZeroFunction extends ConstantFunction {
    public ZeroFunction() {
        super(0);
    }
}