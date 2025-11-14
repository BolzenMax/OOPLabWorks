package ru.ssau.tk.labwork.ooplabworks.dto;

public class UserDTO {

    private Integer id;
    private String login;
    private String role;
    private String password;
    private boolean enabled;

    public UserDTO(Integer id, String login, String role, String password, boolean enabled) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.password = password;
        this.enabled = enabled;
    }

    public UserDTO(String login, String role, String password, boolean enabled) {
        this(null, login, role, password, enabled);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}