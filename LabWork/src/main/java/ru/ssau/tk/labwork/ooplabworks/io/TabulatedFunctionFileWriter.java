package ru.ssau.tk.labwork.ooplabworks.io;

import ru.ssau.tk.labwork.ooplabworks.functions.ArrayTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TabulatedFunctionFileWriter {
    public static void main(String[] args) {
        try (FileWriter fileWriter1 = new FileWriter("LabWork/output/array function.txt");
             FileWriter fileWriter2 = new FileWriter("LabWork/output/linked list function.txt")) {

            BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
            BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);

            // Создаём табулированные функции
            double[] x = {0.0, 1.0, 2.0, 3.0};
            double[] y = {0.0, 1.0, 4.0, 9.0}; // y = x^2

            TabulatedFunction arrayFunction = new ArrayTabulatedFunction(x, y);
            TabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(x, y);

            // Записываем в файлы
            FunctionsIO.writeTabulatedFunction(bufferedWriter1, arrayFunction);
            FunctionsIO.writeTabulatedFunction(bufferedWriter2, linkedListFunction);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
