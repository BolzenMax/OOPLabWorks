package ru.ssau.tk.labwork.ooplabworks.io;

import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.TabulatedFunctionFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
}
