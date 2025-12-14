package ru.ssau.tk.labwork.ooplabworks.ui;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ssau.tk.labwork.ooplabworks.functions.Insertable;
import ru.ssau.tk.labwork.ooplabworks.functions.Removeable;
import ru.ssau.tk.labwork.ooplabworks.functions.TabulatedFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFunctionPayload;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.TabulatedFunctionResponse;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.UiPoint;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.InvalidPointCountException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TabulatedFunctionUiMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TabulatedFunction toFunction(TabulatedFunctionPayload payload, TabulatedFunctionFactory factory) {
        if (payload == null || payload.getPoints() == null || payload.getPoints().size() < 2) {
            throw new InvalidPointCountException("Добавьте минимум две точки перед выполнением операции");
        }

        int count = payload.getPoints().size();
        double[] x = new double[count];
        double[] y = new double[count];

        for (int i = 0; i < count; i++) {
            UiPoint point = payload.getPoints().get(i);
            if (point == null) {
                throw new InvalidPointCountException("В таблице присутствует пустая строка");
            }
            x[i] = point.getX();
            y[i] = point.getY();
        }

        return factory.create(x, y);
    }

    public TabulatedFunctionResponse toResponse(TabulatedFunction function) {
        return toResponse(function, null);
    }

    public TabulatedFunctionResponse toResponse(TabulatedFunction function, String displayName) {
        int count = function.getCount();
        List<UiPoint> points = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            points.add(new UiPoint(function.getX(i), function.getY(i)));
        }
        return new TabulatedFunctionResponse(points, function.leftBound(), function.rightBound(),
                function instanceof Insertable, function instanceof Removeable, displayName);
    }

    public String payloadToJson(TabulatedFunctionPayload payload) throws java.io.IOException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
    }

    public TabulatedFunctionPayload jsonToPayload(String json) throws java.io.IOException {
        return objectMapper.readValue(json, TabulatedFunctionPayload.class);
    }

    public TabulatedFunctionPayload xmlToPayload(String xml) {
        Pattern namePattern = Pattern.compile("<name>(.*?)</name>", Pattern.DOTALL);
        Pattern pattern = Pattern.compile("<point>\\s*<x>(.*?)</x>\\s*<y>(.*?)</y>\\s*</point>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(xml);
        List<UiPoint> points = new ArrayList<>();
        while (matcher.find()) {
            points.add(new UiPoint(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2))));
        }
        TabulatedFunctionPayload payload = new TabulatedFunctionPayload();
        payload.setPoints(points);
        Matcher nameMatcher = namePattern.matcher(xml);
        if (nameMatcher.find()) {
            payload.setName(nameMatcher.group(1));
        }
        return payload;
    }
}