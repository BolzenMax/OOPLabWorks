package ru.ssau.tk.labwork.ooplabworks.concurrent;

import ru.ssau.tk.labwork.ooplabworks.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.MathFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.UnitFunction;

import java.util.ArrayList;
import java.util.List;

public class MultiplyingTaskExecutor {
    public static void main(String[] args) throws InterruptedException {
        MathFunction source = new UnitFunction();
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(source, 1, 1000, 250 );
        List<Thread> threads = new ArrayList<>();
        for(int i = 0; i < 10; i++){

            MultiplyingTask task = new MultiplyingTask(function);

            Thread thread = new Thread(task);

            threads.add(thread);

        }
        for (Thread thread : threads) thread.start();
        Thread.sleep(2000);

        System.out.println(function);

    }
}
