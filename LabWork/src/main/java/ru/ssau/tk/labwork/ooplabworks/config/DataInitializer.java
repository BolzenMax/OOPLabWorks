package ru.ssau.tk.labwork.ooplabworks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.ssau.tk.labwork.ooplabworks.entities.User;
import ru.ssau.tk.labwork.ooplabworks.services.UserService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userService.getUserByLogin("admin").isEmpty()) { // admin
            User admin = new User();
            admin.setLogin("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole("ADMIN");
            admin.setEnabled(true);
            userService.createUser(admin);
            System.out.println("тестовый админ создан");
        }

        if (userService.getUserByLogin("testuser").isEmpty()) { // user
            User testUser = new User();
            testUser.setLogin("testuser");
            testUser.setPassword(passwordEncoder.encode("testpass"));
            testUser.setRole("CIVIL");
            testUser.setEnabled(true);
            userService.createUser(testUser);
            System.out.println("тестовый юзер создан");
        }
    }
}