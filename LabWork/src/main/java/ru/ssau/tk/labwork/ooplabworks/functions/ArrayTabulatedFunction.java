package ru.ssau.tk.labwork.ooplabworks.functions;

import ru.ssau.tk.labwork.ooplabworks.exceptions.InterpolationException;
import java.util.Arrays;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removeable {
    private double[] xValues;
    private double[] yValues;
    private int count;

    // Конструктор с двумя массивами
    public ArrayTabulatedFunction(double[] xValues, double[] yValues) {
        if (xValues.length < 2 && yValues.length < 2) throw new IllegalArgumentException();

        checkLengthIsTheSame(xValues, yValues);
        checkSorted(xValues);

        this.count = xValues.length;
        this.xValues = Arrays.copyOf(xValues, count);
        this.yValues = Arrays.copyOf(yValues, count);
    }

    // Конструктор с функцией и интервалом пвпа
    public ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 2) throw new IllegalArgumentException();

        this.count = count;
        this.xValues = new double[count];
        this.yValues = new double[count];

        // Меняем местами если xFrom > xTo
        if (xFrom > xTo) {
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }

        // Равномерная дискретизация
        if (xFrom == xTo) {
            // Все точки одинаковые
            double value = source.apply(xFrom);
            Arrays.fill(xValues, xFrom);
            Arrays.fill(yValues, value);
        } else {
            double step = (xTo - xFrom) / (count - 1);
            for (int i = 0; i < count; i++) {
                xValues[i] = xFrom + i * step;
                yValues[i] = source.apply(xValues[i]);
            }
        }
    }

    // Реализация методов интерфейса TabulatedFunction
    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double getX(int index) {
        if(index >= 0 && index < count)
            return xValues[index];
        else
            throw new IndexOutOfBoundsException();

    }

    @Override
    public double getY(int index) {
        if (index >= 0 && index < count)
            return yValues[index];
        else
            throw new IndexOutOfBoundsException();
    }

    @Override
    public void setY(int index, double value) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }
        yValues[index] = value;
    }

    @Override
    public int indexOfX(double x) {
        for (int i = 0; i < count; i++) {
            if (Math.abs(xValues[i] - x) < 1e-10) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        for (int i = 0; i < count; i++) {
            if (Math.abs(yValues[i] - y) < 1e-10) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return xValues[0];
    }

    @Override
    public double rightBound() {
        return xValues[count - 1];
    }

    // Реализация абстрактных методов AbstractTabulatedFunction
    @Override
    protected int floorIndexOfX(double x) {
        if (x < xValues[0]) {
            throw new IndexOutOfBoundsException();
        }
        if (x > xValues[count - 1]) {
            throw new IndexOutOfBoundsException();
        }

        for (int i = 1; i < count; i++) {
            if (x < xValues[i]) {
                return i - 1;
            }
        }
        return count - 1;
    }

    @Override
    protected double extrapolateLeft(double x) {
        return interpolate(x, xValues[0], xValues[1], yValues[0], yValues[1]);
    }

    @Override
    protected double extrapolateRight(double x) {
        return interpolate(x, xValues[count - 2], xValues[count - 1], yValues[count - 2], yValues[count - 1]);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (floorIndex == 0) {
            return extrapolateLeft(x);
        }
        if (floorIndex == count) {
            return extrapolateRight(x);
        }

        if (floorIndex < 0 || floorIndex > count || x < xValues[floorIndex] || x > xValues[floorIndex + 1])
            throw new InterpolationException("Не верный диапазон интерполирования");

        double leftX = xValues[floorIndex];
        double rightX = xValues[floorIndex + 1];
        double leftY = yValues[floorIndex];
        double rightY = yValues[floorIndex + 1];

        return interpolate(x, leftX, rightX, leftY, rightY);
    }

    @Override
    public void insert(double x, double y) {
        int index = 0;

        while (index < count && xValues[index] < x) {
            index++;
        }

        if (index < count && Math.abs(xValues[index] - x) < 1e-10) { // тот же х
            yValues[index] = y;
            return;
        }

        double[] newXValues = new double[count + 1];
        double[] newYValues = new double[count + 1];

        System.arraycopy(xValues, 0, newXValues, 0, index); // копирование до вставки
        System.arraycopy(yValues, 0, newYValues, 0, index);

        newXValues[index] = x; // вставка
        newYValues[index] = y;

        System.arraycopy(xValues, index, newXValues, index + 1, count - index); // остальное
        System.arraycopy(yValues, index, newYValues, index + 1, count - index);

        xValues = newXValues; // обновление
        yValues = newYValues;
        count++;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        // Сдвигаем элементы влево
        for (int i = index; i < count - 1; i++) {
            xValues[i] = xValues[i + 1];
            yValues[i] = yValues[i + 1];
        }

        count--;
    }
}