package ru.ssau.tk.labwork.ooplabworks.operations;

import ru.ssau.tk.labwork.ooplabworks.functions.MathFunction;

public interface DifferentialOperator<T extends MathFunction> {
    T derive(T function);
}