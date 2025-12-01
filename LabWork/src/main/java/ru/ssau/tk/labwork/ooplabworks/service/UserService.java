package ru.ssau.tk.labwork.ooplabworks.service;

import org.mindrot.jbcrypt.BCrypt;
import ru.ssau.tk.labwork.ooplabworks.dao.UsersDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcUsersDao;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final UsersDao usersDao;

    public UserService(DataSource ds) {
        this.usersDao = new JdbcUsersDao(ds);
    }

    public UserDTO getById(int id) {
        return usersDao.findById(id).orElse(null);
    }

    public UserDTO getByLogin(String login) {
        return usersDao.findByLogin(login).orElse(null);
    }

    public List<UserDTO> getAll() {
        return usersDao.findAll();
    }

    public List<UserDTO> getAllSortedBy(String field) {
        return usersDao.findAllSortedBy(field);
    }

    public UserDTO create(String login, String role, String rawPassword, boolean enabled) {
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
        UserDTO user = new UserDTO(null, login, role, hashedPassword, enabled);
        usersDao.insert(user);
        return user;
    }

    // --- UPDATE: если меняется — хэшируем ---
    public UserDTO update(int id, String newRawPassword, String newRole, Boolean enabled) {
        UserDTO existing = getById(id);
        if (existing == null) return null;

        if (newRawPassword != null) {
            String hashed = BCrypt.hashpw(newRawPassword, BCrypt.gensalt(12));
            usersDao.updatePassword(id, hashed);
        }
        if (newRole != null) usersDao.updateRole(id, newRole);
        if (enabled != null) usersDao.updateEnabled(id, enabled);

        return getById(id);
    }

    public UserDTO authenticate(String login, String password) {
        UserDTO user = getByLogin(login);
        if (user == null || !user.isEnabled()) return null;
        // plaintext compare
        if (user.getPassword() != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean delete(int id) {
        return usersDao.delete(id);
    }
}
