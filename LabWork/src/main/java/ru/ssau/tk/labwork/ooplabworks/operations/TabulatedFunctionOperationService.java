package ru.ssau.tk.labwork.ooplabworks.operations;

import ru.ssau.tk.labwork.ooplabworks.functions.Point;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;

public class TabulatedFunctionOperationService {

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

    /*public TabulatedFunction multiplication(TabulatedFunction a, TabulatedFunction b) { // умножение
        return doOperation(a, b, (u, v) -> u * v);
    }

    public TabulatedFunction division(TabulatedFunction a, TabulatedFunction b) { // деление
        return doOperation(a, b, (u, v) -> u / v);
    }*/
}
