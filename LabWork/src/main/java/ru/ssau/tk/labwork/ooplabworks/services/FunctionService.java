package ru.ssau.tk.labwork.ooplabworks.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssau.tk.labwork.ooplabworks.entities.Function;
import ru.ssau.tk.labwork.ooplabworks.repositories.FunctionRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FunctionService {

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private PointService pointService;

    public Function createFunction(Function function) {
        log.info("Создание новой функции и присвоение ей ID: {}", function.getUserId());
        Function savedFunction = functionRepository.save(function);
        log.info("Успешно создана функция с ID: {}", savedFunction.getId());
        return savedFunction;
    }

    public Optional<Function> getFunctionById(Long id) {
        log.debug("Получение функции с ID: {}", id);
        return functionRepository.findById(id);
    }

    /*public List<Function> getFunctionsByUserId(Long userId) {
        log.debug("Получение функций пользователя с ID: {}", userId);
        return functionRepository.findByUserId(userId);
    }

    public List<Function> searchFunctionsByName(String name) {
        log.debug("Поиск функций по имени: {}", name);
        return functionRepository.findByNameContaining(name);
    }

    public List<Function> searchFunctionsByUserIdAndName(Long userId, String name) {
        log.debug("Поиск функций по пользовательскому ID: {} с именем: {}", userId, name);
        return functionRepository.findByUserIdAndNameContaining(userId, name);
    }*/

    public List<Function> getAllFunctions() {
        log.debug("Поиск всех функций");
        return functionRepository.findAll();
    }

    public Function updateFunction(Function function) {
        log.info("Актуализация функции с ID: {}", function.getId());
        Function updatedFunction = functionRepository.save(function);
        log.info("Успешная актуализация");
        return updatedFunction;
    }

    public void deleteFunction(Long id) {
        log.info("Удаление функции с ID: {}", id);
        pointService.deletePointsByFunctionId(id);
        functionRepository.deleteById(id);
        log.info("Успешное удаление");
    }

    public Long countFunctionsByUserId(Long userId) {
        return functionRepository.countByUserId(userId);
    }
}