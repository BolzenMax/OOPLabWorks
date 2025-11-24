package ru.ssau.tk.labwork.ooplabworks.dto;

public class UserRequest {
    private String login;
    private String password;
    private String role;
    private boolean enabled;

    public UserRequest() {}

    public UserRequest(String login, String password, String role, boolean enabled) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}