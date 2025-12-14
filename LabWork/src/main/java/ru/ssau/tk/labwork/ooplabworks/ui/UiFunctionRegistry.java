package ru.ssau.tk.labwork.ooplabworks.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import ru.ssau.tk.labwork.ooplabworks.functions.CompositeFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.MathFunction;
import ru.ssau.tk.labwork.ooplabworks.functions.UiMathFunction;
import ru.ssau.tk.labwork.ooplabworks.ui.dto.MathFunctionOption;
import ru.ssau.tk.labwork.ooplabworks.ui.exceptions.UnknownFunctionException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class UiFunctionRegistry {
    private final Map<String, MathFunction> functions = new HashMap<>();
    private final String basePackage;

    public UiFunctionRegistry(@Value("ru.ssau.tk.labwork.ooplabworks.functions") String basePackage) {
        this.basePackage = basePackage;
        scanFunctions();
    }

    public synchronized List<MathFunctionOption> getOptions() {
        List<MathFunctionOption> options = new ArrayList<>();
        functions.entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<String, MathFunction> e) -> getPriority(e.getKey())).reversed()
                        .thenComparing(Map.Entry::getKey))
                .forEach(entry -> options.add(new MathFunctionOption(entry.getKey(), entry.getKey())));
        return options;
    }

    public synchronized MathFunction resolve(String displayName) {
        return Optional.ofNullable(functions.get(displayName))
                .orElseThrow(() -> new UnknownFunctionException("Неизвестная функция: " + displayName));
    }

    public synchronized void addComposite(String name, String outerName, String innerName) {
        if (name == null || name.isBlank()) {
            throw new UnknownFunctionException("Введите название сложной функции");
        }
        if (functions.containsKey(name)) {
            throw new UnknownFunctionException("Функция с таким названием уже существует");
        }
        MathFunction outer = resolve(outerName);
        MathFunction inner = resolve(innerName);
        functions.put(name, new CompositeFunction(outer, inner));
    }

    private void scanFunctions() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(UiMathFunction.class));
        Set<org.springframework.beans.factory.config.BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
        for (org.springframework.beans.factory.config.BeanDefinition candidate : candidates) {
            try {
                Class<?> clazz = Class.forName(candidate.getBeanClassName());
                UiMathFunction meta = clazz.getAnnotation(UiMathFunction.class);
                if (meta != null && MathFunction.class.isAssignableFrom(clazz)) {
                    functions.put(meta.displayName(), (MathFunction) clazz.getDeclaredConstructor().newInstance());
                }
            } catch (Exception ignored) {
                // skip broken class
            }
        }
    }

    private int getPriority(String displayName) {
        try {
            MathFunction function = functions.get(displayName);
            UiMathFunction meta = function.getClass().getAnnotation(UiMathFunction.class);
            return meta != null ? meta.priority() : 0;
        } catch (Exception ex) {
            return 0;
        }
    }
}