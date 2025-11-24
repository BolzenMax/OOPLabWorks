package ru.ssau.tk.labwork.ooplabworks.service;

import ru.ssau.tk.labwork.ooplabworks.dao.UsersDao;
import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcUsersDao;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;

import javax.sql.DataSource;
import java.util.List;

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

    public UserDTO create(String login, String role, String password, boolean enabled) {
        UserDTO user = new UserDTO(null, login, role, password, enabled);
        usersDao.insert(user);
        return user;
    }

    public UserDTO update(int id, String newPassword, String newRole, Boolean enabled) {
        UserDTO existing = getById(id);
        if (existing == null) return null;

        if (newPassword != null) usersDao.updatePassword(id, newPassword);
        if (newRole != null) usersDao.updateRole(id, newRole);
        if (enabled != null) usersDao.updateEnabled(id, enabled);

        return getById(id);
    }

    public boolean delete(int id) {
        return usersDao.delete(id);
    }
}
