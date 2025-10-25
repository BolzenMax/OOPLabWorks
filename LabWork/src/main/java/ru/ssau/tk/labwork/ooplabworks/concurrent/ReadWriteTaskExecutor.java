package ru.ssau.tk.labwork.ooplabworks.concurrent;

import ru.ssau.tk.labwork.ooplabworks.functions.ConstantFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;

public class ReadWriteTaskExecutor {
    public static void main(String[] args) {
        TabulatedFunction tabulatedFunction = new LinkedListTabulatedFunction(new ConstantFunction(-1.0), 1.0, 1000.0, 1000);

        ReadTask readTask = new ReadTask(tabulatedFunction);
        WriteTask writeTask = new WriteTask(tabulatedFunction, 0.5);

        Thread readThread = new Thread(readTask, "ReadThread");
        Thread writeThread = new Thread(writeTask, "WriteThread");

        writeThread.start();
        readThread.start();

        try {
            writeThread.join();
            readThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}