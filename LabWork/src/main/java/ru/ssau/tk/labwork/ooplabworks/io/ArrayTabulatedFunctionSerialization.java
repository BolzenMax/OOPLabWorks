package ru.ssau.tk.labwork.ooplabworks.io;

import ru.ssau.tk.labwork.ooplabworks.functions.ArrayTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.labwork.ooplabworks.operations.TabulatedDifferentialOperator;

import java.io.*;

public class ArrayTabulatedFunctionSerialization {
    public static void main(String[] args) {
        String filePath = "LabWork/output/serialized array functions.bin";

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {

            double[] x = {0.0, 1.0, 2.0, 3.0, 4.0};
            double[] y = {0.0, 1.0, 4.0, 9.0, 16.0}; // y = x^2
            TabulatedFunction original = new ArrayTabulatedFunctionFactory().create(x, y);

            TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
            TabulatedFunction firstDerivative = operator.derive(original);
            TabulatedFunction secondDerivative = operator.derive(firstDerivative);

            FunctionsIO.serialize(bufferedOutputStream, original);
            FunctionsIO.serialize(bufferedOutputStream, firstDerivative);
            FunctionsIO.serialize(bufferedOutputStream, secondDerivative);

        } catch (IOException e) {
            e.printStackTrace();
        }


        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {

            TabulatedFunction original = FunctionsIO.deserialize(bufferedInputStream);
            TabulatedFunction firstDerivative = FunctionsIO.deserialize(bufferedInputStream);
            TabulatedFunction secondDerivative = FunctionsIO.deserialize(bufferedInputStream);

            System.out.println("Original function:");
            System.out.println(original);
            System.out.println("\nFirst derivative:");
            System.out.println(firstDerivative);
            System.out.println("\nSecond derivative:");
            System.out.println(secondDerivative);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
