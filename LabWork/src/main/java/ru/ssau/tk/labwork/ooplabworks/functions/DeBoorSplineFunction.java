package ru.ssau.tk.labwork.ooplabworks.functions;

public class DeBoorSplineFunction implements MathFunction {
    private final double[] knots;
    private final double[] controlPoints;
    private final int degree;

    public DeBoorSplineFunction(double[] knots, double[] controlPoints, int degree) {
        if (knots.length != controlPoints.length + degree + 1) {
            throw new IllegalArgumentException("Invalid number of knots. Expected: " + (controlPoints.length + degree + 1) + ", got: " + knots.length);
        }
        this.knots = knots.clone();
        this.controlPoints = controlPoints.clone();
        this.degree = degree;
    }

    @Override
    public double apply(double x) {
        return deBoorAlgorithm(x);
    }

    private double deBoorAlgorithm(double x) {
        // Находим интервал узлов [knots[k], knots[k+1]), содержащий x
        int k = findKnotInterval(x);

        // Экстраполяция слева
        if (k < degree) {
            return controlPoints[0];
        }

        // Экстраполяция справа
        if (k >= knots.length - degree - 1) {
            return controlPoints[controlPoints.length - 1];
        }

        // Копируем контрольные точки для алгоритма
        double[] d = new double[degree + 1];
        for (int i = 0; i <= degree; i++) {
            d[i] = controlPoints[k - degree + i];
        }

        // Алгоритм де Бура
        for (int r = 1; r <= degree; r++) {
            for (int j = degree; j >= r; j--) {
                int i = k - degree + j;
                double alpha = (x - knots[i]) / (knots[i + degree - r + 1] - knots[i]);
                d[j] = (1 - alpha) * d[j - 1] + alpha * d[j];
            }
        }

        return d[degree];
    }

    private int findKnotInterval(double x) {
        // Для экстраполяции справа
        if (x >= knots[knots.length - degree - 1]) {
            return knots.length - degree - 2;
        }

        // Для экстраполяции слева
        if (x < knots[degree]) {
            return degree;
        }

        // Ищем интервал для интерполяции
        for (int i = degree; i < knots.length - degree - 1; i++) {
            if (x >= knots[i] && x < knots[i + 1]) {
                return i;
            }
        }

        return degree; // fallback
    }

    public double[] getKnots() {
        return knots.clone();
    }

    public double[] getControlPoints() {
        return controlPoints.clone();
    }

    public int getDegree() {
        return degree;
    }
}