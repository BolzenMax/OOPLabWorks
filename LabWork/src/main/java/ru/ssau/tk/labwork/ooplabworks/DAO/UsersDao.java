package ru.ssau.tk.labwork.ooplabworks.DAO;

public interface UsersDao {
    int insert(String login, String role, String password, boolean enabled);
    //Optional<User> findById(int id);
    //Optional<User> findByLogin(String login);
    boolean updatePassword(int id, String newPassword);
    boolean updateRole(int id, String newRole);
    boolean updateEnabled(int id, boolean enabled);
    boolean delete(int id);
}