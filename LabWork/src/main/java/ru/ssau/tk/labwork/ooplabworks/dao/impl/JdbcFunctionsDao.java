package ru.ssau.tk.labwork.ooplabworks.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ssau.tk.labwork.ooplabworks.dao.FunctionsDao;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class JdbcFunctionsDao implements FunctionsDao {

    private final DataSource ds;
    private static final Logger log = LoggerFactory.getLogger(JdbcFunctionsDao.class);

    public JdbcFunctionsDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public int insert(FunctionDTO f) {
        String sql = "INSERT INTO functions (user_id, name, signature) VALUES (?, ?, ?) RETURNING id";

        log.info("INSERT Function userId={} name={}", f.getUserId(), f.getName());

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, f.getUserId());
            ps.setString(2, f.getName());
            ps.setString(3, f.getSignature());

            ResultSet rs = ps.executeQuery();
            rs.next();
            int id = rs.getInt(1);
            f.setId(id);

            log.debug("Function created id={}", id);
            return id;

        } catch (SQLException e) {
            log.error("insert function failed", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FunctionDTO> findById(int id) {
        String sql = "SELECT * FROM functions WHERE id = ?";

        log.debug("SELECT Function id={}", id);

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();

        } catch (SQLException e) {
            log.error("findById failed", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FunctionDTO> findByUserId(int userId) {
        String sql = "SELECT * FROM functions WHERE user_id = ?";

        log.debug("SELECT Functions by userId={}", userId);

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            List<FunctionDTO> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));

            return list;

        } catch (SQLException e) {
            log.error("findByUserId failed", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateName(int id, String name) {
        return execUpdate("UPDATE functions SET name = ? WHERE id = ?", name, id);
    }

    @Override
    public boolean updateSignature(int id, String signature) {
        return execUpdate("UPDATE functions SET signature = ? WHERE id = ?", signature, id);
    }

    @Override
    public boolean delete(int id) {
        return execUpdate("DELETE FROM functions WHERE id = ?", id);
    }

    private boolean execUpdate(String sql, Object... params) {
        log.info("UPDATE/DELETE Function: {}", sql);

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

    private FunctionDTO map(ResultSet rs) throws SQLException {
        return new FunctionDTO(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("name"),
                rs.getString("signature")
        );
    }
    @Override
    public List<FunctionDTO> findByName(String name) {
        String sql = "SELECT * FROM functions WHERE name = ?";

        log.debug("SELECT Functions by name={}", name);

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            List<FunctionDTO> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));

            return list;

        } catch (SQLException e) {
            log.error("findByName failed", e);
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<FunctionDTO> findByNames(Collection<String> names) {
        if (names == null || names.isEmpty()) return List.of();

        String placeholders = String.join(",", Collections.nCopies(names.size(), "?"));
        String sql = "SELECT * FROM functions WHERE name IN (" + placeholders + ")";

        log.debug("SELECT Functions by names={}", names);

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            int index = 1;
            for (String n : names) {
                ps.setString(index++, n);
            }

            ResultSet rs = ps.executeQuery();

            List<FunctionDTO> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));

            return list;

        } catch (SQLException e) {
            log.error("findByNames failed", e);
            throw new RuntimeException(e);
        }
    }

}