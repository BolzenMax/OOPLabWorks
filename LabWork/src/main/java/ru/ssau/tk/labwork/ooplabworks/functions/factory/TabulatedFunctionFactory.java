package ru.ssau.tk.labwork.ooplabworks.functions.factory;

import ru.ssau.tk.labwork.ooplabworks.functions.MathFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);

    TabulatedFunction create(MathFunction source, double xFrom, double xTo, int count);
}