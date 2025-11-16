package ru.ssau.tk.labwork.ooplabworks.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ssau.tk.labwork.ooplabworks.dao.UsersDao;
import ru.ssau.tk.labwork.ooplabworks.dto.UserDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class JdbcUsersDao implements UsersDao {

    private final DataSource ds;
    private static final Logger log = LoggerFactory.getLogger(JdbcUsersDao.class);

    public JdbcUsersDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public int insert(UserDTO user) {
        String sql = "INSERT INTO users (login, role, password, enabled) VALUES (?, ?, ?, ?) RETURNING id";

        log.info("INSERT User: login={}", user.getLogin());

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, user.getLogin());
            ps.setString(2, user.getRole());
            ps.setString(3, user.getPassword());
            ps.setBoolean(4, user.isEnabled());

            ResultSet rs = ps.executeQuery();
            rs.next();
            int id = rs.getInt(1);

            user.setId(id);
            log.debug("User inserted with id={}", id);

            return id;

        } catch (SQLException e) {
            log.error("User insert failed", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserDTO> findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        log.debug("SELECT User by id={}", id);

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(map(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            log.error("findById error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserDTO> findByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";

        log.debug("SELECT User by login={}", login);

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(map(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            log.error("findByLogin error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserDTO> findAll() {
        String sql = "SELECT * FROM users";

        log.debug("SELECT All Users");

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<UserDTO> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;

        } catch (SQLException e) {
            log.error("findAll error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updatePassword(int id, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        return execUpdate(sql, newPassword, id);
    }

    @Override
    public boolean updateRole(int id, String newRole) {
        String sql = "UPDATE users SET role = ? WHERE id = ?";
        return execUpdate(sql, newRole, id);
    }

    @Override
    public boolean updateEnabled(int id, boolean enabled) {
        String sql = "UPDATE users SET enabled = ? WHERE id = ?";
        return execUpdate(sql, enabled, id);
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return execUpdate(sql, id);
    }

    private boolean execUpdate(String sql, Object... params) {
        log.info("UPDATE/DELETE: {}", sql);

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++)
                ps.setObject(i + 1, params[i]);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            log.error("execUpdate failed", e);
            throw new RuntimeException(e);
        }
    }

    private UserDTO map(ResultSet rs) throws SQLException {
        return new UserDTO(
                rs.getInt("id"),
                rs.getString("login"),
                rs.getString("role"),
                rs.getString("password"),
                rs.getBoolean("enabled")
        );
    }
    @Override
    public List<UserDTO> findAllSortedBy(String field) {
        log.debug("findAllSortedBy field={}", field);

        // защищаемся от SQL injection
        if (!Set.of("login", "role", "enabled", "id").contains(field)) {
            throw new IllegalArgumentException("Invalid sort field: " + field);
        }

        String sql = "SELECT * FROM users ORDER BY " + field;

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<UserDTO> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;

        } catch (SQLException e) {
            log.error("findAllSortedBy failed", e);
            throw new RuntimeException(e);
        }
    }
}
