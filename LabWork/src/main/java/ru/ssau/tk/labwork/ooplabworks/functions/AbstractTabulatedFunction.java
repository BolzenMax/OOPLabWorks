package ru.ssau.tk.labwork.ooplabworks.functions;

import ru.ssau.tk.labwork.ooplabworks.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.labwork.ooplabworks.exceptions.DifferentLengthOfArraysException;

public abstract class AbstractTabulatedFunction implements TabulatedFunction {
    protected abstract int floorIndexOfX(double x);

    protected abstract double extrapolateLeft(double x);

    protected abstract double extrapolateRight(double x);

    protected abstract double interpolate(double x, int floorIndex);

    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
    }

    protected int count;
    static void checkLengthIsTheSame(double[] xValues, double[] yValues){
        if(xValues.length != yValues.length) throw new DifferentLengthOfArraysException("Разная длина массивов!");
    }



    static void checkSorted(double[] xValues){
        for (int i = 1; i < xValues.length; i++){
            if(xValues[i] < xValues[i-1]) throw new ArrayIsNotSortedException("Массив не отсортирован!");

        }
    }


    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else {
            int index = indexOfX(x);
            if (index != -1) {
                return getY(index);
            } else {
                return interpolate(x, floorIndexOfX(x));
            }
        }
    }
}