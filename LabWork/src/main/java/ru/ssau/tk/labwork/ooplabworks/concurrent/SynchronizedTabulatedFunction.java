package ru.ssau.tk.labwork.ooplabworks.concurrent;

import ru.ssau.tk.labwork.ooplabworks.functions.Point;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.operations.TabulatedFunctionOperationService;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SynchronizedTabulatedFunction implements TabulatedFunction {

    private final TabulatedFunction function;
    private final Object mutex;

    public SynchronizedTabulatedFunction(TabulatedFunction function) {
        this.function = function;
        this.mutex = this;
    }

    @FunctionalInterface
    public interface Operation<T> {
        T apply(SynchronizedTabulatedFunction function);
    }

    public <T> T doSynchronously(Operation<? extends T> operation) {
        synchronized (mutex) {
            return operation.apply(this);
        }
    }

    @Override
    public int getCount() {
        synchronized (mutex) {
            return function.getCount();
        }
    }

    @Override
    public double getX(int index) {
        synchronized (mutex) {
            return function.getX(index);
        }
    }

    @Override
    public double getY(int index) {
        synchronized (mutex) {
            return function.getY(index);
        }
    }

    @Override
    public void setY(int index, double value) {
        synchronized (mutex) {
            function.setY(index, value);
        }
    }

    @Override
    public int indexOfX(double x) {
        synchronized (mutex) {
            return function.indexOfX(x);
        }
    }

    @Override
    public int indexOfY(double y) {
        synchronized (mutex) {
            return function.indexOfY(y);
        }
    }

    @Override
    public double leftBound() {
        synchronized (mutex) {
            return function.leftBound();
        }
    }

    @Override
    public double rightBound() {
        synchronized (mutex) {
            return function.rightBound();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        synchronized (mutex) {
            Point[] copyPoints = TabulatedFunctionOperationService.asPoints(function);

            return new Iterator<Point>() {
                private int currIndex = 0;
                private final Point[] points = copyPoints;

                @Override
                public boolean hasNext() {
                    return currIndex < points.length;
                }

                @Override
                public Point next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException("No more elements");
                    }
                    return points[currIndex++];
                }
            };
        }
    }

    @Override
    public double apply(double x) {
        synchronized (mutex) {
            return function.apply(x);
        }
    }
}