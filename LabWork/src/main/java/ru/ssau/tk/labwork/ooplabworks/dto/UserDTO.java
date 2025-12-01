package ru.ssau.tk.labwork.ooplabworks.dto;

public class UserDTO {
    private Long id;
    private String login;
    private String password;
    private String role;
    private boolean enabled;

    public UserDTO() {}

    public UserDTO(Long id, String login, String role, boolean enabled) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.enabled = enabled;
    }

    public UserDTO(String login, String password, String role, boolean enabled) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}