package ru.ssau.tk.labwork.ooplabworks.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;
import ru.ssau.tk.labwork.ooplabworks.entities.Function;
import ru.ssau.tk.labwork.ooplabworks.services.FunctionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/functions")
public class FunctionController {

    private static final Logger log = LoggerFactory.getLogger(FunctionController.class);

    @Autowired
    private FunctionService functionService;

    @GetMapping
    public ResponseEntity<List<FunctionDTO>> getAllFunctions() {
        log.info("Получение всех функций");
        List<FunctionDTO> functions = functionService.getAllFunctions().stream()
                .map(func -> new FunctionDTO(
                        func.getId(),
                        func.getUserId(),
                        func.getName(),
                        func.getSignature()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(functions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FunctionDTO> getFunctionById(@PathVariable Long id) {
        log.info("Получение функции с ID: {}", id);
        Optional<Function> function = functionService.getFunctionById(id);
        if (function.isPresent()) {
            FunctionDTO response = new FunctionDTO(
                    function.get().getId(),
                    function.get().getUserId(),
                    function.get().getName(),
                    function.get().getSignature()
            );
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FunctionDTO>> getFunctionsByUserId(@PathVariable Long userId) {
        log.info("Получение функций пользователя с ID: {}", userId);
        List<Function> functions = functionService.getAllFunctions().stream()
                .filter(func -> func.getUserId().equals(userId))
                .collect(Collectors.toList());

        List<FunctionDTO> responses = functions.stream()
                .map(func -> new FunctionDTO(
                        func.getId(),
                        func.getUserId(),
                        func.getName(),
                        func.getSignature()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<FunctionDTO> createFunction(@RequestBody FunctionDTO functionRequest) {
        log.info("Создание функции для пользователя с ID: {}", functionRequest.getUserId());

        Function function = new Function(
                functionRequest.getUserId(),
                functionRequest.getName(),
                functionRequest.getSignature()
        );

        Function savedFunction = functionService.createFunction(function);
        FunctionDTO response = new FunctionDTO(
                savedFunction.getId(),
                savedFunction.getUserId(),
                savedFunction.getName(),
                savedFunction.getSignature()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FunctionDTO> updateFunction(@PathVariable Long id, @RequestBody FunctionDTO functionRequest) {
        log.info("Обновление функции с ID: {}", id);

        Optional<Function> existingFunction = functionService.getFunctionById(id);
        if (existingFunction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Function function = existingFunction.get();
        function.setUserId(functionRequest.getUserId());
        function.setName(functionRequest.getName());
        function.setSignature(functionRequest.getSignature());

        Function updatedFunction = functionService.updateFunction(function);
        FunctionDTO response = new FunctionDTO(
                updatedFunction.getId(),
                updatedFunction.getUserId(),
                updatedFunction.getName(),
                updatedFunction.getSignature()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFunction(@PathVariable Long id) {
        log.info("Удаление функции с ID: {}", id);

        Optional<Function> function = functionService.getFunctionById(id);
        if (function.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        functionService.deleteFunction(id);
        return ResponseEntity.noContent().build();
    }
}