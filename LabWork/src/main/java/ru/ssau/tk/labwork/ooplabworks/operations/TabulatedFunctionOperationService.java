package ru.ssau.tk.labwork.ooplabworks.operations;

import ru.ssau.tk.labwork.ooplabworks.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.labwork.ooplabworks.functions.Point;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.TabulatedFunctionFactory;

public class TabulatedFunctionOperationService {


    private TabulatedFunctionFactory factory;

    public TabulatedFunctionOperationService() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    public TabulatedFunctionOperationService(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public static Point[] asPoints(TabulatedFunction tabulatedFunction) {
        if (tabulatedFunction == null) {
            return new Point[0];
        }

        Point[] points = new Point[tabulatedFunction.getCount()];
        int i = 0;
        for (Point point : tabulatedFunction) {
            points[i] = point;
            i++;
        }
        return points;
    }

    @FunctionalInterface
    private interface BiOperation {
        double apply(double u, double v);
    }

    private TabulatedFunction doOperation(TabulatedFunction a, TabulatedFunction b, BiOperation operation) {
        if (a.getCount() != b.getCount()) {
            throw new InconsistentFunctionsException("Функции имеют разное количество точек");
        }

        Point[] pointsA = asPoints(a);
        Point[] pointsB = asPoints(b);
        int n = pointsA.length;

        double[] xValues = new double[n];
        double[] yValues = new double[n];

        for (int i = 0; i < n; i++) {
            if (Math.abs(pointsA[i].x - pointsB[i].x) > 1e-10) {
                throw new InconsistentFunctionsException("Функции имеют разные значения x в точке " + i);
            }
            xValues[i] = pointsA[i].x;
            yValues[i] = operation.apply(pointsA[i].y, pointsB[i].y);
        }

        return factory.create(xValues, yValues);
    }

    public TabulatedFunction add(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u + v);
    }

    public TabulatedFunction subtract(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u - v);
    }

    public TabulatedFunction multiplication(TabulatedFunction a, TabulatedFunction b) { // умножение
        return doOperation(a, b, (u, v) -> u * v);
    }

    public TabulatedFunction division(TabulatedFunction a, TabulatedFunction b) { // деление
        return doOperation(a, b, (u, v) -> u / v);
    }
}
