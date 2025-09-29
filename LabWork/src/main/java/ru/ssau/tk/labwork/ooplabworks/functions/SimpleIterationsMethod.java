package ru.ssau.tk.labwork.ooplabworks.functions;

public class SimpleIterationsMethod implements MathFunction {
    private final MathFunction g;
    private final double accuracy;
    private final int maxIterations;

    public double apply(double x) { // вспомогательный метод
        return g.apply(x);
    }

    public SimpleIterationsMethod (MathFunction g, double accuracy, int maxIterations) {
        this.g = g;
        this.accuracy = accuracy;
        this.maxIterations = maxIterations;
    }

    public double findRoot(double initial) {
        double x = initial;
        int iterations = 0;

        while (iterations < maxIterations) {
            double nextX = g.apply(x);  // вычисление следующего приближения
            if (Math.abs(nextX - x) < accuracy)
                return nextX;
            x = nextX;
            iterations++;
        }
        return x;
    }
}