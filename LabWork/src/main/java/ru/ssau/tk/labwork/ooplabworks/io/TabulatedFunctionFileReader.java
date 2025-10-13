package ru.ssau.tk.labwork.ooplabworks.io;

import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.LinkedListTabulatedFunctionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TabulatedFunctionFileReader {

    public static void main(String[] args) {
        try (FileReader fileReader1 = new FileReader("LabWork/input/function.txt");
             FileReader fileReader2 = new FileReader("LabWork/input/function.txt")) {

            BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
            BufferedReader bufferedReader2 = new BufferedReader(fileReader2);

            TabulatedFunction arrayFunction = FunctionsIO.readTabulatedFunction(
                    bufferedReader1,
                    new ArrayTabulatedFunctionFactory()
            );

            TabulatedFunction linkedListFunction = FunctionsIO.readTabulatedFunction(
                    bufferedReader2,
                    new LinkedListTabulatedFunctionFactory()
            );

            System.out.println(arrayFunction);
            System.out.println(linkedListFunction);

        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
