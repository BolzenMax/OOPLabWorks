package ru.ssau.tk.labwork.ooplabworks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ssau.tk.labwork.ooplabworks.entities.Point;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findByFunctionId(Long functionId);

    @Query("SELECT p FROM Point p WHERE p.functionId = :functionId AND p.x BETWEEN :minX AND :maxX")
    List<Point> findByFunctionIdAndXBetween(@Param("functionId") Long functionId,
                                            @Param("minX") Double minX,
                                            @Param("maxX") Double maxX);

    @Query("SELECT p FROM Point p WHERE p.functionId = :functionId ORDER BY p.x ASC")
    List<Point> findByFunctionIdOrderByXAsc(@Param("functionId") Long functionId);

    @Query("SELECT p FROM Point p WHERE p.functionId = :functionId ORDER BY p.x DESC")
    List<Point> findByFunctionIdOrderByXDesc(@Param("functionId") Long functionId);

    void deleteByFunctionId(Long functionId);
}