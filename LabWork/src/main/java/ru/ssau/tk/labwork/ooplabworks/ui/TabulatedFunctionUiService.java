package ru.ssau.tk.labwork.ooplabworks.ui;

import org.springframework.stereotype.Service;
import ru.ssau.tk.labwork.ooplabworks.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.labwork.ooplabworks.exceptions.DifferentLengthOfArraysException;
import ru.ssau.tk.labwork.ooplabworks.functions.MathFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.MathFunctionOption;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.NewCompositeFunctionRequest;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFromArrayRequest;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFromFunctionRequest;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedApplyRequest;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFunctionResponse;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.InvalidPointCountException;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.NonNumericValueException;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.TableSizeMismatchException;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.UnknownFunctionException;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.ValueOutOfRangeException;

import java.util.List;

@Service
public class TabulatedFunctionUiService {
    private static final int MIN_POINTS = 2;
    private static final int MAX_POINTS = 1000;
    private static final double MAX_ABSOLUTE_VALUE = 1e9;

    private final UiFactoryService factoryService;
    private final TabulatedFunctionUiMapper mapper;
    private final UiFunctionRegistry functionRegistry;

    public TabulatedFunctionUiService(UiFactoryService factoryService, TabulatedFunctionUiMapper mapper,
                                      UiFunctionRegistry functionRegistry) {
        this.factoryService = factoryService;
        this.mapper = mapper;
        this.functionRegistry = functionRegistry;
    }

    public List<MathFunctionOption> getFunctionOptions() {
        return functionRegistry.getOptions();
    }

    public TabulatedFunctionResponse createFromArrays(TabulatedFromArrayRequest request) {
        int count = validatePointsCount(request.getPointsCount());
        double[] xValues = readNumbers(request, count, "x");
        double[] yValues = readNumbers(request, count, "y");

        try {
            TabulatedFunction function = factoryService.currentFactory().create(xValues, yValues);
            return mapper.toResponse(function, request.getName());
        } catch (ArrayIsNotSortedException | DifferentLengthOfArraysException ex) {
            throw ex;
        } catch (IllegalArgumentException ex) {
            throw new TableSizeMismatchException("Невозможно построить функцию из переданных значений: " + ex.getMessage());
        }
    }

    public TabulatedFunctionResponse createFromFunction(TabulatedFromFunctionRequest request) {
        int count = validatePointsCount(request.getPointsCount());
        MathFunction source = resolveFunction(request.getFunctionName());

        double from = parseNumber(request.getFrom(), "Начало интервала", -1);
        double to = parseNumber(request.getTo(), "Конец интервала", -1);

        TabulatedFunction function = factoryService.currentFactory().create(source, from, to, count);
        return mapper.toResponse(function, request.getName());
    }

    public void createComposite(NewCompositeFunctionRequest request) {
        functionRegistry.addComposite(request.getDisplayName(), request.getOuterFunction(), request.getInnerFunction());
    }

    public double apply(TabulatedApplyRequest request) {
        if (request == null || request.getPayload() == null) {
            throw new UnknownFunctionException("Функция не передана");
        }
        double x = parseNumber(request.getX(), "x", -1);
        TabulatedFunction function = mapper.toFunction(request.getPayload(), factoryService.currentFactory());
        return function.apply(x);
    }

    private int validatePointsCount(Integer count) {
        if (count == null) {
            throw new InvalidPointCountException("Введите количество точек");
        }
        if (count < MIN_POINTS) {
            throw new InvalidPointCountException("Количество точек не может быть меньше " + MIN_POINTS);
        }
        if (count > MAX_POINTS) {
            throw new InvalidPointCountException("Количество точек ограничено " + MAX_POINTS + " для удобства работы");
        }
        return count;
    }

    private double[] readNumbers(TabulatedFromArrayRequest request, int expected, String axis) {
        List<String> values = request.getXValues();
        List<String> fallback = request.getYValues();

        List<String> chosen = axis.startsWith("x") ? values : fallback;

        if (chosen == null || chosen.size() != expected) {
            if (request.getPoints() != null && request.getPoints().size() == expected) {
                double[] parsed = new double[expected];
                for (int i = 0; i < expected; i++) {
                    double value = axis.startsWith("x") ? request.getPoints().get(i).getX() : request.getPoints().get(i).getY();
                    parsed[i] = parseNumber(Double.toString(value), axis + "" + (i + 1), i);
                }
                return parsed;
            }
            throw new TableSizeMismatchException("Ожидалось " + expected + " значений " + axis + ", но получено " +
                    (chosen == null ? 0 : chosen.size()));
        }

        double[] parsed = new double[expected];
        for (int i = 0; i < expected; i++) {
            parsed[i] = parseNumber(chosen.get(i), axis + "" + (i + 1), i);
        }
        return parsed;
    }

    private double parseNumber(String rawValue, String fieldName, int index) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new NonNumericValueException("Поле '" + fieldName + "' не заполнено");
        }
        double parsed;
        try {
            parsed = Double.parseDouble(rawValue.replace(",", "."));
        } catch (NumberFormatException ex) {
            throw new NonNumericValueException("Поле '" + fieldName + "' должно быть числом");
        }
        if (!Double.isFinite(parsed) || Math.abs(parsed) > MAX_ABSOLUTE_VALUE) {
            throw new ValueOutOfRangeException("Значение '" + fieldName + "' выходит за допустимый диапазон ±" + MAX_ABSOLUTE_VALUE);
        }
        return parsed;
    }

    private MathFunction resolveFunction(String displayName) {
        if (displayName == null) {
            throw new UnknownFunctionException("Функция не выбрана");
        }
        return functionRegistry.resolve(displayName);
    }
}