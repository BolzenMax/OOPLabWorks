package ru.ssau.tk.labwork.ooplabworks.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;
import ru.ssau.tk.labwork.ooplabworks.entities.User;
import ru.ssau.tk.labwork.ooplabworks.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Получение всех пользователей");
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getLogin(),
                        user.getRole(),
                        user.isEnabled()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("Получение пользователя с ID: {}", id);
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            UserDTO response = new UserDTO(
                    user.get().getId(),
                    user.get().getLogin(),
                    user.get().getRole(),
                    user.get().isEnabled()
            );
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userRequest) {
        log.info("Создание пользователя: {}", userRequest.getLogin());

        User user = new User();
        user.setLogin(userRequest.getLogin());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        user.setEnabled(userRequest.isEnabled());

        User savedUser = userService.createUser(user);
        UserDTO response = new UserDTO(
                savedUser.getId(),
                savedUser.getLogin(),
                savedUser.getRole(),
                savedUser.isEnabled()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userRequest) {
        log.info("Обновление пользователя с ID: {}", id);

        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = existingUser.get();
        user.setLogin(userRequest.getLogin());
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        user.setRole(userRequest.getRole());
        user.setEnabled(userRequest.isEnabled());

        User updatedUser = userService.updateUser(user);
        UserDTO response = new UserDTO(
                updatedUser.getId(),
                updatedUser.getLogin(),
                updatedUser.getRole(),
                updatedUser.isEnabled()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Удаление пользователя с ID: {}", id);

        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}