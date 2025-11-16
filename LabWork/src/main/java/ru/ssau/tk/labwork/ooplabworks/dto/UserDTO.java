package ru.ssau.tk.labwork.ooplabworks.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDTO {

    private static final Logger log = LoggerFactory.getLogger(UserDTO.class);

    private Integer id;
    private String login;
    private String role;
    private String password;
    private boolean enabled;

    public UserDTO(Integer id, String login, String role, String password, boolean enabled) {
        log.debug("[UserDTO] ctor(id={}, login={}, role={}, enabled={})",
                id, login, role, enabled);

        this.id = id;
        this.login = login;
        this.role = role;
        this.password = password;
        this.enabled = enabled;
    }

    public UserDTO(String login, String role, String password, boolean enabled) {
        this(null, login, role, password, enabled);
    }

    public Integer getId() {
        log.trace("[UserDTO] getId() -> {}", id);
        return id;
    }

    public void setId(Integer id) {
        log.debug("[UserDTO] setId({}) old={}", id, this.id);
        this.id = id;
    }

    public String getLogin() {
        log.trace("[UserDTO] getLogin() -> {}", login);
        return login;
    }

    public void setLogin(String login) {
        log.debug("[UserDTO] setLogin({}) old={}", login, this.login);
        this.login = login;
    }

    public String getRole() {
        log.trace("[UserDTO] getRole() -> {}", role);
        return role;
    }

    public void setRole(String role) {
        log.debug("[UserDTO] setRole({}) old={}", role, this.role);
        this.role = role;
    }

    public String getPassword() {
        log.trace("[UserDTO] getPassword() -> {}", password);
        return password;
    }

    public void setPassword(String password) {
        log.debug("[UserDTO] setPassword(******) old=******");
        this.password = password;
    }

    public boolean isEnabled() {
        log.trace("[UserDTO] isEnabled() -> {}", enabled);
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        log.debug("[UserDTO] setEnabled({}) old={}", enabled, this.enabled);
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        String s = "UserDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                '}';
        log.trace("[UserDTO] toString() -> {}", s);
        return s;
    }
}
