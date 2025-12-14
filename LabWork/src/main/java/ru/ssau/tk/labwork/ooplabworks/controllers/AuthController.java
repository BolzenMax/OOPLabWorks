package ru.ssau.tk.labwork.ooplabworks.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.labwork.ooplabworks.config.CustomUserDetails;
import ru.ssau.tk.labwork.ooplabworks.dto.AuthDTO;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;
import ru.ssau.tk.labwork.ooplabworks.entities.User;
import ru.ssau.tk.labwork.ooplabworks.services.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetails userDetails;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userRequest) {
        log.info("Регистрация нового пользователя: {}", userRequest.getLogin());

        if (userService.userExists(userRequest.getLogin())) {
            log.warn("Пользователь с логином {} уже существует", userRequest.getLogin());
            return ResponseEntity.badRequest().body("Пользователь с таким логином уже существует");
        }

        if (userRequest.getPassword() == null || userRequest.getConfirmPassword() == null ||
                !userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Пароли не совпадают");
        }

        User user = new User();
        user.setLogin(userRequest.getLogin());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole("CIVIL");
        user.setEnabled(true);

        User savedUser = userService.createUser(user);
        UserDTO response = new UserDTO(
                savedUser.getId(),
                savedUser.getLogin(),
                savedUser.getRole(),
                savedUser.isEnabled()
        );

        log.info("Пользователь успешно зарегистрирован с ID: {}", savedUser.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO authRequest) {
        log.info("Попытка входа пользователя: {}", authRequest.getLogin());

        try {
            Optional<User> user = userService.getUserByLogin(authRequest.getLogin());
            if (user.isEmpty()) {
                return ResponseEntity.status(401).body("Неверные учетные данные");
            }

            if (!user.get().isEnabled() || !passwordEncoder.matches(authRequest.getPassword(), user.get().getPassword())) {
                log.warn("Ошибка входа для пользователя: {}", authRequest.getLogin());
                return ResponseEntity.status(401).body("Неверные учетные данные");
            }

            userDetails.loadUserByUsername(authRequest.getLogin());

            UserDTO response = new UserDTO(
                    user.get().getId(),
                    user.get().getLogin(),
                    user.get().getRole(),
                    user.get().isEnabled()
            );
            log.info("Успешный вход пользователя: {}", authRequest.getLogin());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.warn("Ошибка входа для пользователя: {}", authRequest.getLogin());
            return ResponseEntity.status(401).body("Неверные учетные данные");
        }
    }
}