package ru.ssau.tk.labwork.ooplabworks.concurrent;

import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;

public class WriteTask implements Runnable {

    private final TabulatedFunction function;
    private final double value;

    public WriteTask (TabulatedFunction function, double value) {
        this.function = function;
        this.value = value;
    }

    @Override
    public void run() {
        for (int i = 0; i < function.getCount(); i++) {
            synchronized (function) {
                function.setY(i, value);
                System.out.printf("Writing for index %d complete\n", i);
                System.out.flush();
            }
        }
    }
}