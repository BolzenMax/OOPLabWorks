package ru.ssau.tk.labwork.ooplabworks.dto;

public class UserResponse {
    private Long id;
    private String login;
    private String role;
    private boolean enabled;

    public UserResponse() {}

    public UserResponse(Long id, String login, String role, boolean enabled) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.enabled = enabled;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}