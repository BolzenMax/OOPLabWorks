package ru.ssau.tk.labwork.ooplabworks.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ssau.tk.labwork.ooplabworks.entities.User;
import ru.ssau.tk.labwork.ooplabworks.services.UserService;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Загрузка пользователя по логину: {}", username);
        Optional<User> user = userService.getUserByLogin(username);
        if (user.isEmpty()) {
            log.warn("Пользователь с логином {} не найден", username);
            throw new UsernameNotFoundException("Пользователь не найден: " + username);
        }
        return new CustomUserDetails(user.get());
    }
}