package ru.ssau.tk.labwork.ooplabworks.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.labwork.ooplabworks.dto.PointDTO;
import ru.ssau.tk.labwork.ooplabworks.entities.Point;
import ru.ssau.tk.labwork.ooplabworks.services.PointService;
import ru.ssau.tk.labwork.ooplabworks.entities.Function;
import ru.ssau.tk.labwork.ooplabworks.services.FunctionService;
import ru.ssau.tk.labwork.ooplabworks.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.security.Principal;

@RestController
@RequestMapping("/api/points")
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    @Autowired
    private PointService pointService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private UserService userService;

    public ResponseEntity<List<PointDTO>> getAllPoints(Principal principal) {
        log.info("Получение всех точек пользователя {}", principal.getName());
        Long userId = userService.getUserByLogin(principal.getName()).map(u -> u.getId()).orElse(null);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        List<PointDTO> points = pointService.getAllPoints().stream()
                .filter(point -> belongsToUser(point, userId))
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
    public ResponseEntity<PointDTO> getPointById(@PathVariable Long id, Principal principal) {
        log.info("Получение точки с ID: {}", id);
        Optional<Point> point = pointService.getPointById(id);
        if (point.isPresent() && isOwner(principal, point.get())) {
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
    public ResponseEntity<List<PointDTO>> getPointsByFunctionId(@PathVariable Long functionId, Principal principal) {
        log.info("Получение точек функции с ID: {}", functionId);
        if (!ownsFunction(principal, functionId)) {
            return ResponseEntity.status(403).build();
        }

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
    public ResponseEntity<PointDTO> createPoint(@RequestBody PointDTO pointRequest, Principal principal) {
        log.info("Создание точки для функции с ID: {}", pointRequest.getFunctionId());
        if (!ownsFunction(principal, pointRequest.getFunctionId())) {
            return ResponseEntity.status(403).build();
        }

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
    public ResponseEntity<List<PointDTO>> createPoints(@RequestBody List<PointDTO> pointRequests, Principal principal) {
        log.info("Создание {} точек", pointRequests.size());

        if (pointRequests.stream().anyMatch(req -> !ownsFunction(principal, req.getFunctionId()))) {
            return ResponseEntity.status(403).build();
        }

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
    public ResponseEntity<PointDTO> updatePoint(@PathVariable Long id, @RequestBody PointDTO pointRequest, Principal principal) {
        log.info("Обновление точки с ID: {}", id);

        Optional<Point> existingPoint = pointService.getPointById(id);
        if (existingPoint.isEmpty() || !isOwner(principal, existingPoint.get())) {
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
    public ResponseEntity<Void> deletePoint(@PathVariable Long id, Principal principal) {
        log.info("Удаление точки с ID: {}", id);

        Optional<Point> point = pointService.getPointById(id);
        if (point.isEmpty() || !isOwner(principal, point.get())) {
            return ResponseEntity.notFound().build();
        }

        pointService.deletePoint(id);
        return ResponseEntity.noContent().build();
    }

    private boolean ownsFunction(Principal principal, Long functionId) {
        if (principal == null) {
            return false;
        }
        Optional<Function> function = functionService.getFunctionById(functionId);
        return function.filter(f -> isOwner(principal, f)).isPresent();
    }

    private boolean isOwner(Principal principal, Function function) {
        return userService.getUserByLogin(principal.getName())
                .map(user -> user.getId().equals(function.getUserId()))
                .orElse(false);
    }

    private boolean isOwner(Principal principal, Point point) {
        return ownsFunction(principal, point.getFunctionId());
    }

    private boolean belongsToUser(Point point, Long userId) {
        return functionService.getFunctionById(point.getFunctionId())
                .map(f -> f.getUserId().equals(userId))
                .orElse(false);
    }
}