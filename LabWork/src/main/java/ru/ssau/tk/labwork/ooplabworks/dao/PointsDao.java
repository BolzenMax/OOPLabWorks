package ru.ssau.tk.labwork.ooplabworks.dao;


import ru.ssau.tk.labwork.ooplabworks.dto.PointDTO;
import java.util.List;
import java.util.Optional;

public interface PointsDao {

    // CREATE
    int insert(PointDTO point);

    // READ
    Optional<PointDTO> findById(int id);
    List<PointDTO> findByFunctionId(int functionId);

    // UPDATE
    boolean update(int id, double x, double y);

    // DELETE
    boolean delete(int id);
    boolean deleteByFunction(int functionId);
}