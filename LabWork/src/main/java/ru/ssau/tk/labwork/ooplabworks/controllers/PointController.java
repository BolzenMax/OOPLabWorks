package ru.ssau.tk.labwork.ooplabworks.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.labwork.ooplabworks.dto.PointRequest;
import ru.ssau.tk.labwork.ooplabworks.dto.PointResponse;
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
    public ResponseEntity<List<PointResponse>> getAllPoints() {
        log.info("Получение всех точек");
        List<PointResponse> points = pointService.getAllPoints().stream()
                .map(point -> new PointResponse(
                        point.getId(),
                        point.getFunctionId(),
                        point.getX(),
                        point.getY()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(points);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PointResponse> getPointById(@PathVariable Long id) {
        log.info("Получение точки с ID: {}", id);
        Optional<Point> point = pointService.getPointById(id);
        if (point.isPresent()) {
            PointResponse response = new PointResponse(
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
    public ResponseEntity<List<PointResponse>> getPointsByFunctionId(@PathVariable Long functionId) {
        log.info("Получение точек функции с ID: {}", functionId);
        List<Point> points = pointService.getAllPoints().stream()
                .filter(point -> point.getFunctionId().equals(functionId))
                .collect(Collectors.toList());

        List<PointResponse> responses = points.stream()
                .map(point -> new PointResponse(
                        point.getId(),
                        point.getFunctionId(),
                        point.getX(),
                        point.getY()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<PointResponse> createPoint(@RequestBody PointRequest pointRequest) {
        log.info("Создание точки для функции с ID: {}", pointRequest.getFunctionId());

        Point point = new Point(
                pointRequest.getFunctionId(),
                pointRequest.getX(),
                pointRequest.getY()
        );

        Point savedPoint = pointService.createPoint(point);
        PointResponse response = new PointResponse(
                savedPoint.getId(),
                savedPoint.getFunctionId(),
                savedPoint.getX(),
                savedPoint.getY()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<PointResponse>> createPoints(@RequestBody List<PointRequest> pointRequests) {
        log.info("Создание {} точек", pointRequests.size());

        List<Point> points = pointRequests.stream()
                .map(req -> new Point(req.getFunctionId(), req.getX(), req.getY()))
                .collect(Collectors.toList());

        List<Point> savedPoints = pointService.createPoints(points);
        List<PointResponse> responses = savedPoints.stream()
                .map(point -> new PointResponse(
                        point.getId(),
                        point.getFunctionId(),
                        point.getX(),
                        point.getY()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PointResponse> updatePoint(@PathVariable Long id, @RequestBody PointRequest pointRequest) {
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
        PointResponse response = new PointResponse(
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