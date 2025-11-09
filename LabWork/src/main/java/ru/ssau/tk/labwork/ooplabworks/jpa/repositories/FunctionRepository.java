package ru.ssau.tk.labwork.ooplabworks.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ssau.tk.labwork.ooplabworks.jpa.entities.Function;

import java.util.List;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Long> {

    List<Function> findByUserId(Long userId);

    List<Function> findByNameContaining(String name);

    @Query("SELECT f FROM Function f WHERE f.userId = :userId AND f.name LIKE %:name%")
    List<Function> findByUserIdAndNameContaining(@Param("userId") Long userId,
                                                 @Param("name") String name);
}