package ru.ssau.tk.labwork.ooplabworks.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ssau.tk.labwork.ooplabworks.dao.PointsDao;
import ru.ssau.tk.labwork.ooplabworks.dto.PointDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class JdbcPointsDao implements PointsDao {

    private final DataSource ds;
    private static final Logger log = LoggerFactory.getLogger(JdbcPointsDao.class);

    public JdbcPointsDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public int insert(PointDTO point) {
        String sql = "INSERT INTO points (function_id, x_value, y_value) VALUES (?, ?, ?) RETURNING id";

        log.info("INSERT Point functionId={}", point.getFunctionId());

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, point.getFunctionId());
            ps.setDouble(2, point.getX());
            ps.setDouble(3, point.getY());

            ResultSet rs = ps.executeQuery();
            rs.next();
            int id = rs.getInt(1);
            point.setId(id);

            log.debug("Point inserted id={}", id);

            return id;

        } catch (SQLException e) {
            log.error("insert point failed", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<PointDTO> findById(int id) {
        String sql = "SELECT * FROM points WHERE id = ?";

        log.debug("SELECT Point id={}", id);

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
    public List<PointDTO> findByFunctionId(int functionId) {
        String sql = "SELECT * FROM points WHERE function_id = ?";

        log.debug("SELECT Points by functionId={}", functionId);

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, functionId);
            ResultSet rs = ps.executeQuery();

            List<PointDTO> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));

            return list;

        } catch (SQLException e) {
            log.error("findByFunctionId failed", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(int id, double x, double y) {
        return execUpdate(
                "UPDATE points SET x_value = ?, y_value = ? WHERE id = ?",
                x, y, id
        );
    }

    @Override
    public boolean delete(int id) {
        return execUpdate("DELETE FROM points WHERE id = ?", id);
    }

    @Override
    public boolean deleteByFunction(int functionId) {
        return execUpdate("DELETE FROM points WHERE function_id = ?", functionId);
    }

    private boolean execUpdate(String sql, Object... params) {
        log.info("UPDATE/DELETE Point: {}", sql);

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++)
                ps.setObject(i + 1, params[i]);

            return ps.executeUpdate() >= 1;

        } catch (SQLException e) {
            log.error("execUpdate point failed", e);
            throw new RuntimeException(e);
        }
    }

    private PointDTO map(ResultSet rs) throws SQLException {
        return new PointDTO(
                rs.getInt("id"),
                rs.getInt("function_id"),
                rs.getDouble("x_value"),
                rs.getDouble("y_value")
        );
    }
}