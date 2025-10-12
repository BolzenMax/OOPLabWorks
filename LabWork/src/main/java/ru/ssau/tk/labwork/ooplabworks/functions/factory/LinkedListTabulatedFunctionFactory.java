package ru.ssau.tk.labwork.ooplabworks.functions.factory;

import ru.ssau.tk.labwork.ooplabworks.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;

public class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {
    @Override
    public TabulatedFunction create(double[] xValues, double[] yValues) {
        return new LinkedListTabulatedFunction(xValues, yValues);
    }
}