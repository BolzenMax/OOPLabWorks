package ru.ssau.tk.labwork.ooplabworks.service;

import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcPointsDao;
import ru.ssau.tk.labwork.ooplabworks.dto.PointDTO;

import javax.sql.DataSource;
import java.util.List;

public class PointService {

    private final JdbcPointsDao dao;

    public PointService(DataSource ds) {
        this.dao = new JdbcPointsDao(ds);
    }

    public PointDTO create(int functionId, double x, double y) {
        PointDTO p = new PointDTO(null, functionId, x, y);
        dao.insert(p);
        return p;
    }

    public PointDTO getById(int id) {
        return dao.findById(id).orElse(null);
    }

    public List<PointDTO> getByFunctionId(int functionId) {
        return dao.findByFunctionId(functionId);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }

    public boolean deleteByFunction(int functionId) {
        return dao.deleteByFunction(functionId);
    }

    public PointDTO update(int id, double x, double y) {
        PointDTO existing = dao.findById(id).orElse(null);
        if (existing == null) return null;

        dao.update(id, x, y);
        return dao.findById(id).orElse(null);
    }
}
