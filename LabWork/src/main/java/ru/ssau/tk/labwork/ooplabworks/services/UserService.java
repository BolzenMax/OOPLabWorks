package ru.ssau.tk.labwork.ooplabworks.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssau.tk.labwork.ooplabworks.entities.User;
import ru.ssau.tk.labwork.ooplabworks.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        log.info("Создание нового пользователя с логином: {}", user.getLogin());
        User savedUser = userRepository.save(user);
        log.info("Пользователь создан с присвоением ему ID: {}", savedUser.getId());
        return savedUser;
    }

    public Optional<User> getUserById(Long id) {
        log.debug("Получение пользователя с ID: {}", id);
        return userRepository.findById(id);
    }

    public Optional<User> getUserByLogin(String login) {
        log.debug("Получение пользователя с логином: {}", login);
        return userRepository.findByLogin(login);
    }

    public List<User> getAllUsers() {
        log.debug("Получение всех пользователей");
        return userRepository.findAll();
    }

    public User updateUser(User user) {
        log.info("Актуализация пользователя с ID: {}", user.getId());
        User updatedUser = userRepository.save(user);
        log.info("Успешная актуализация");
        return updatedUser;
    }

    public void deleteUser(Long id) {
        log.info("Удаление пользователя с ID: {}", id);
        userRepository.deleteById(id);
        log.info("Пользователь успешно удалён");
    }

    public boolean userExists(String login) {
        return userRepository.existsByLogin(login);
    }

    public void createUsers(List<User> users) {
    }
}