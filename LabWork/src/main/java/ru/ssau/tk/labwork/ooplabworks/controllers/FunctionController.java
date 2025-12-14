package ru.ssau.tk.labwork.ooplabworks.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;
import ru.ssau.tk.labwork.ooplabworks.entities.Function;
import ru.ssau.tk.labwork.ooplabworks.services.FunctionService;
import org.springframework.http.HttpStatus;
import ru.ssau.tk.labwork.ooplabworks.entities.User;
import ru.ssau.tk.labwork.ooplabworks.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.security.Principal;

@RestController
@RequestMapping("/api/functions")
public class FunctionController {

    private static final Logger log = LoggerFactory.getLogger(FunctionController.class);

    @Autowired
    private FunctionService functionService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<FunctionDTO>> getAllFunctions(Principal principal) {
        log.info("Получение всех функций пользователя {}", principal.getName());
        Long userId = userService.getUserByLogin(principal.getName())
                .map(User::getId)
                .orElse(null);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<FunctionDTO> functions = functionService.getFunctionsByUserId(userId).stream()
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
    public ResponseEntity<FunctionDTO> getFunctionById(@PathVariable Long id, Principal principal) {
        log.info("Получение функции с ID: {}", id);
        Optional<Function> function = functionService.getFunctionById(id);
        if (function.isPresent() && isOwner(principal, function.get())) {
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
    public ResponseEntity<List<FunctionDTO>> getFunctionsByUserId(@PathVariable Long userId, Principal principal) {
        log.info("Получение функций пользователя с ID: {}", userId);
        Long requesterId = userService.getUserByLogin(principal.getName()).map(User::getId).orElse(null);
        if (requesterId == null || !requesterId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<FunctionDTO> responses = functionService.getFunctionsByUserId(userId).stream()
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
    public ResponseEntity<FunctionDTO> createFunction(@RequestBody FunctionDTO functionRequest, Principal principal) {
        Long userId = userService.getUserByLogin(principal.getName()).map(User::getId).orElse(null);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Создание функции для пользователя с ID: {}", userId);
        Function function = new Function(
                userId,
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
    public ResponseEntity<FunctionDTO> updateFunction(@PathVariable Long id, @RequestBody FunctionDTO functionRequest, Principal principal) {
        log.info("Обновление функции с ID: {}", id);

        Optional<Function> existingFunction = functionService.getFunctionById(id);
        if (existingFunction.isEmpty() || !isOwner(principal, existingFunction.get())) {
            return ResponseEntity.notFound().build();
        }

        Function function = existingFunction.get();
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
    public ResponseEntity<Void> deleteFunction(@PathVariable Long id, Principal principal) {
        log.info("Удаление функции с ID: {}", id);

        Optional<Function> function = functionService.getFunctionById(id);
        if (function.isEmpty() || !isOwner(principal, function.get())) {
            return ResponseEntity.notFound().build();
        }

        functionService.deleteFunction(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isOwner(Principal principal, Function function) {
        if (principal == null) {
            return false;
        }
        return userService.getUserByLogin(principal.getName())
                .map(User::getId)
                .filter(id -> id.equals(function.getUserId()))
                .isPresent();
    }
}