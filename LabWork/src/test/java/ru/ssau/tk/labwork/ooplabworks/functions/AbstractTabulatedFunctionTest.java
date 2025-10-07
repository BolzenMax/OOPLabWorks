package ru.ssau.tk.labwork.ooplabworks.functions;
import ru.ssau.tk.labwork.ooplabworks.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.labwork.ooplabworks.exceptions.DifferentLengthOfArraysException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractTabulatedFunctionTest {

    @Test
    void floorIndexOfX() {


    }

    @Test
    void extrapolateLeft() {
    }

    @Test
    void extrapolateRight() {
    }

    @Test
    void interpolate() {
    }

    @Test
    void testInterpolate() {
    }

    @Test
    void checkLengthIsTheSame() {
        double[] x = {1, 2, 2, 1, 23};
        double[] y = {1,3,4,5};
        DifferentLengthOfArraysException exception = assertThrows(
                DifferentLengthOfArraysException.class,
                () -> { AbstractTabulatedFunction.checkLengthIsTheSame(x, y); }
        );
    }

    @Test
    void checkSorted() {
        double[] x = {1, 2, 2, 1, 23};
        ArrayIsNotSortedException exception = assertThrows(
                ArrayIsNotSortedException.class,
                () -> { AbstractTabulatedFunction.checkSorted(x); }
        );
    }

    @Test
    void getCount() {


    }

    @Test
    void apply() {
    }
}