package ru.ssau.tk.labwork.ooplabworks.io;

import ru.ssau.tk.labwork.ooplabworks.functions.Point;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.TabulatedFunctionFactory;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public final class FunctionsIO {

    private FunctionsIO() {
        throw new UnsupportedOperationException("Error");
    }

    public static void writeTabulatedFunction(BufferedWriter bufferedWriter1, TabulatedFunction arrayFunction) {
    }

    public static TabulatedFunction readTabulatedFunction(BufferedReader reader, TabulatedFunctionFactory factory)
            throws IOException {
        String firstLine = reader.readLine();
        int count = Integer.parseInt(firstLine.trim());

        double[] xValues = new double[count];
        double[] yValues = new double[count];

        NumberFormat formatter = NumberFormat.getInstance(Locale.forLanguageTag("ru"));

        for (int i = 0; i < count; i++) {
            String line = reader.readLine();
            String[] parts = line.trim().split(" ");
            try {
                xValues[i] = formatter.parse(parts[0]).doubleValue();
                yValues[i] = formatter.parse(parts[1]).doubleValue();
            } catch (ParseException e) {
                throw new IOException("Parse error at line " + (i + 2), e);
            }
        }

        return factory.create(xValues, yValues);
    }

    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException {
        try (DataOutputStream dataOutput = new DataOutputStream(outputStream)) {
            dataOutput.writeInt(function.getCount());
            for (Point point : function) {
                dataOutput.writeDouble(point.x);
                dataOutput.writeDouble(point.y);
            }
            dataOutput.flush(); // переброска из буфера
        }
    }

    public static TabulatedFunction readTabulatedFunction(BufferedInputStream inputStream, TabulatedFunctionFactory factory) throws IOException {
        DataInputStream stream = new DataInputStream(inputStream);
        int length = stream.readInt();
        double[] xValues = new double[length];
        double[] yValues = new double[length];

        for (int i = 0; i < length; ++i) {
            xValues[i] = stream.readDouble();
            yValues[i] = stream.readDouble();
        }
        return factory.create(xValues, yValues);
    }

    public static TabulatedFunction deserialize(BufferedInputStream stream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(stream);
        return (TabulatedFunction) objectInputStream.readObject();
    }
}