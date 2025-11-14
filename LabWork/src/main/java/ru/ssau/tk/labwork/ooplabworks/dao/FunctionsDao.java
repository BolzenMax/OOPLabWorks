package ru.ssau.tk.labwork.ooplabworks.dao;

import ru.ssau.tk.labwork.ooplabworks.dto.FunctionDTO;

import java.util.List;
import java.util.Optional;

public interface FunctionsDao {

    // CREATE
    int insert(FunctionDTO function);

    // READ
    Optional<FunctionDTO> findById(int id);
    List<FunctionDTO> findByUserId(int userId);

    // UPDATE
    boolean updateName(int id, String newName);
    boolean updateSignature(int id, String newSignature);

    // DELETE
    boolean delete(int id);
}
