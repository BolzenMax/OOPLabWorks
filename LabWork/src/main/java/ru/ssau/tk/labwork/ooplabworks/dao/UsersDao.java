package ru.ssau.tk.labwork.ooplabworks.dao;

import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UsersDao {

    // CREATE
    int insert(UserDTO user);

    // READ
    Optional<UserDTO> findById(int id);
    Optional<UserDTO> findByLogin(String login);
    List<UserDTO> findAll();

    // UPDATE
    boolean updatePassword(int id, String newPassword);
    boolean updateRole(int id, String newRole);
    boolean updateEnabled(int id, boolean enabled);

    // DELETE
    boolean delete(int id);
}