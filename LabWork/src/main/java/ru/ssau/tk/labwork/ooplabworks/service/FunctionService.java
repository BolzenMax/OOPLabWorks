package ru.ssau.tk.labwork.ooplabworks.service;

import ru.ssau.tk.labwork.ooplabworks.dao.impl.JdbcFunctionsDao;
import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;

import javax.sql.DataSource;
import java.util.List;

public class FunctionService {

    private final JdbcFunctionsDao dao;

    public FunctionService(DataSource ds) {
        this.dao = new JdbcFunctionsDao(ds);
    }

    public FunctionDTO create(int userId, String name, String signature) {
        FunctionDTO f = new FunctionDTO(null, userId, name, signature);
        dao.insert(f);
        return f;
    }


    public FunctionDTO getById(int id) {
        return dao.findById(id).orElse(null);
    }

    public List<FunctionDTO> getByUserId(int userId) {
        return dao.findByUserId(userId);
    }

    public List<FunctionDTO> getByName(String name) {
        return dao.findByName(name);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }

    public FunctionDTO update(int id, String name, String signature) {
        FunctionDTO existing = dao.findById(id).orElse(null);
        if (existing == null) return null;

        if (name != null) dao.updateName(id, name);
        if (signature != null) dao.updateSignature(id, signature);

        return dao.findById(id).orElse(null);
    }

}
