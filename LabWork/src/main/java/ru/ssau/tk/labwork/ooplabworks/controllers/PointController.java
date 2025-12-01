package ru.ssau.tk.labwork.ooplabworks.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.labwork.ooplabworks.dto.PointDTO;
import ru.ssau.tk.labwork.ooplabworks.entities.Point;
import ru.ssau.tk.labwork.ooplabworks.services.PointService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/points")
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    @Autowired
    private PointService pointService;

    @GetMapping
    public ResponseEntity<List<PointDTO>> getAllPoints() {
        log.info("Получение всех точек");
        List<PointDTO> points = pointService.getAllPoints().stream()
                .map(point -> new PointDTO(
                        point.getId(),
                        point.getFunctionId(),
                        point.getX(),
                        point.getY()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(points);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PointDTO> getPointById(@PathVariable Long id) {
        log.info("Получение точки с ID: {}", id);
        Optional<Point> point = pointService.getPointById(id);
        if (point.isPresent()) {
            PointDTO response = new PointDTO(
                    point.get().getId(),
                    point.get().getFunctionId(),
                    point.get().getX(),
                    point.get().getY()
            );
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/function/{functionId}")
    public ResponseEntity<List<PointDTO>> getPointsByFunctionId(@PathVariable Long functionId) {
        log.info("Получение точек функции с ID: {}", functionId);
        List<Point> points = pointService.getAllPoints().stream()
                .filter(point -> point.getFunctionId().equals(functionId))
                .collect(Collectors.toList());

        List<PointDTO> responses = points.stream()
                .map(point -> new PointDTO(
                        point.getId(),
                        point.getFunctionId(),
                        point.getX(),
                        point.getY()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<PointDTO> createPoint(@RequestBody PointDTO pointRequest) {
        log.info("Создание точки для функции с ID: {}", pointRequest.getFunctionId());

        Point point = new Point(
                pointRequest.getFunctionId(),
                pointRequest.getX(),
                pointRequest.getY()
        );

        Point savedPoint = pointService.createPoint(point);
        PointDTO response = new PointDTO(
                savedPoint.getId(),
                savedPoint.getFunctionId(),
                savedPoint.getX(),
                savedPoint.getY()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<PointDTO>> createPoints(@RequestBody List<PointDTO> pointRequests) {
        log.info("Создание {} точек", pointRequests.size());

        List<Point> points = pointRequests.stream()
                .map(req -> new Point(req.getFunctionId(), req.getX(), req.getY()))
                .collect(Collectors.toList());

        List<Point> savedPoints = pointService.createPoints(points);
        List<PointDTO> responses = savedPoints.stream()
                .map(point -> new PointDTO(
                        point.getId(),
                        point.getFunctionId(),
                        point.getX(),
                        point.getY()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PointDTO> updatePoint(@PathVariable Long id, @RequestBody PointDTO pointRequest) {
        log.info("Обновление точки с ID: {}", id);

        Optional<Point> existingPoint = pointService.getPointById(id);
        if (existingPoint.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Point point = existingPoint.get();
        point.setFunctionId(pointRequest.getFunctionId());
        point.setX(pointRequest.getX());
        point.setY(pointRequest.getY());

        Point updatedPoint = pointService.updatePoint(point);
        PointDTO response = new PointDTO(
                updatedPoint.getId(),
                updatedPoint.getFunctionId(),
                updatedPoint.getX(),
                updatedPoint.getY()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoint(@PathVariable Long id) {
        log.info("Удаление точки с ID: {}", id);

        Optional<Point> point = pointService.getPointById(id);
        if (point.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pointService.deletePoint(id);
        return ResponseEntity.noContent().build();
    }
}