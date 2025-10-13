package ru.ssau.tk.labwork.ooplabworks.io;

import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.ArrayTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.LinkedListTabulatedFunction;

import java.io.*;

public class TabulatedFunctionFileOutputStream {
    public static void main(String[] args) {
        try (
                FileOutputStream arrayFile = new FileOutputStream("output/array function.bin");
                FileOutputStream linkedListFile = new FileOutputStream("output/linked list function.bin");
                BufferedOutputStream arrayBuffer = new BufferedOutputStream(arrayFile);
                BufferedOutputStream linkedListBuffer = new BufferedOutputStream(linkedListFile)
        ) {
            TabulatedFunction arrayFunction = new ArrayTabulatedFunction(new double[]{0.0, 0.5, 1.0}, new double[]{0.0, 0.25, 1.0}); // создание ф-й
            TabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(new double[]{0.0, 0.5, 1.0}, new double[]{0.0, 0.25, 1.0});

            FunctionsIO.writeTabulatedFunction(arrayBuffer, arrayFunction); // запись в файл
            FunctionsIO.writeTabulatedFunction(linkedListBuffer, linkedListFunction);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}