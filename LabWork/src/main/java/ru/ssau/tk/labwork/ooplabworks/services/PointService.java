package ru.ssau.tk.labwork.ooplabworks.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssau.tk.labwork.ooplabworks.entities.Point;
import ru.ssau.tk.labwork.ooplabworks.repositories.PointRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PointService {

    @Autowired
    private PointRepository pointRepository;

    public Point createPoint(Point point) {
        log.info("Создание новой точки для функции с ID: {}", point.getFunctionId());
        Point savedPoint = pointRepository.save(point);
        log.info("Успешно создана точка с присвоением ей ID: {}", savedPoint.getId());
        return savedPoint;
    }

    public List<Point> createPoints(List<Point> points) {
        log.info("Создание {} точек", points.size());
        List<Point> savedPoints = pointRepository.saveAll(points);
        log.info("Все точки успешно созданы");
        return savedPoints;
    }

    public Optional<Point> getPointById(Long id) {
        log.debug("Поиск точки по её ID: {}", id);
        return pointRepository.findById(id);
    }

    /*public List<Point> getPointsByFunctionId(Long functionId) {
        log.debug("Получение точек функции с ID: {}", functionId);
        return pointRepository.findByFunctionId(functionId);
    }

    public List<Point> getPointsByFunctionIdAndXRange(Long functionId, Double minX, Double maxX) {
        log.debug("Получение точек функции с ID: {} где X в интервале [{}, {}]", functionId, minX, maxX);
        return pointRepository.findByFunctionIdAndXBetween(functionId, minX, maxX);
    }

    public List<Point> getPointsByFunctionIdOrderedByXAsc(Long functionId) {
        log.debug("Получение точек функции с ID: {} где X по возрастанию", functionId);
        return pointRepository.findByFunctionIdOrderByXAsc(functionId);
    }

    public List<Point> getPointsByFunctionIdOrderedByXDesc(Long functionId) {
        log.debug("Получение точек функции с ID: {} где X по убыванию", functionId);
        return pointRepository.findByFunctionIdOrderByXDesc(functionId);
    }*/

    public List<Point> getAllPoints() {
        log.debug("Получение всех точек");
        return pointRepository.findAll();
    }

    public Point updatePoint(Point point) {
        log.info("Актуализация точки с ID: {}", point.getId());
        Point updatedPoint = pointRepository.save(point);
        log.info("Успешная актуализация");
        return updatedPoint;
    }

    public void deletePoint(Long id) {
        log.info("Удаление точки с ID: {}", id);
        pointRepository.deleteById(id);
        log.info("Точка успешно удалена");
    }

    public void deletePointsByFunctionId(Long functionId) {
        log.info("Удаление всех точек функции с ID: {}", functionId);
        pointRepository.deleteByFunctionId(functionId);
        log.info("Успешно удалены все точки функции с ID: {}", functionId);
    }
}