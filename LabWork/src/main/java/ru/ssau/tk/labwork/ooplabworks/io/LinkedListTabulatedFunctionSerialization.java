package ru.ssau.tk.labwork.ooplabworks.io;

import ru.ssau.tk.labwork.ooplabworks.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.labwork.ooplabworks.operations.TabulatedDifferentialOperator;

import java.io.*;

public class LinkedListTabulatedFunctionSerialization {

    public static void main(String[] args) {
        try (FileOutputStream fileOutput = new FileOutputStream("output/serialized linked list functions.bin");
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput)) {
            TabulatedFunction function = new LinkedListTabulatedFunction(new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 4.0, 9.0});
            TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());

            TabulatedFunction firstDerivative = operator.derive(function);
            TabulatedFunction secondDerivative = operator.derive(firstDerivative);

            FunctionsIO.serialize(bufferedOutput, function);
            FunctionsIO.serialize(bufferedOutput, firstDerivative);
            FunctionsIO.serialize(bufferedOutput, secondDerivative);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream fileInput = new FileInputStream("output/serialized linked list functions.bin");
             BufferedInputStream bufferedInput = new BufferedInputStream(fileInput)) {
            TabulatedFunction deserializedFunction = FunctionsIO.deserialize(bufferedInput);
            TabulatedFunction deserializedFirstDerivative = FunctionsIO.deserialize(bufferedInput);
            TabulatedFunction deserializedSecondDerivative = FunctionsIO.deserialize(bufferedInput);

            System.out.println("Исходная функция:");
            System.out.println(deserializedFunction);
            System.out.println("Первая производная:");
            System.out.println(deserializedFirstDerivative);
            System.out.println("Вторая производная:");
            System.out.println(deserializedSecondDerivative);
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}