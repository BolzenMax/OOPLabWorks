package ru.ssau.tk.labwork.ooplabworks.ui;

import org.springframework.stereotype.Service;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.labwork.ooplabworks.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.InvalidFactorySelectionException;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UiFactoryService {

    private final Map<TabulatedFactoryType, TabulatedFunctionFactory> factories = new EnumMap<>(TabulatedFactoryType.class);
    private final AtomicReference<TabulatedFactoryType> currentType = new AtomicReference<>(TabulatedFactoryType.ARRAY);

    public UiFactoryService() {
        factories.put(TabulatedFactoryType.ARRAY, new ArrayTabulatedFunctionFactory());
        factories.put(TabulatedFactoryType.LINKED_LIST, new LinkedListTabulatedFunctionFactory());
    }

    public TabulatedFunctionFactory currentFactory() {
        return factories.get(currentType.get());
    }

    public TabulatedFactoryType getCurrentType() {
        return currentType.get();
    }

    public TabulatedFactoryType updateType(TabulatedFactoryType type) {
        if (type == null || !factories.containsKey(type)) {
            throw new InvalidFactorySelectionException("Не удалось выбрать фабрику табуляции");
        }
        currentType.set(type);
        return type;
    }
}