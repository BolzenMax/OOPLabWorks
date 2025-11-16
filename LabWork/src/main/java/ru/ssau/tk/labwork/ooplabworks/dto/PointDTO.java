package ru.ssau.tk.labwork.ooplabworks.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointDTO {

    private static final Logger log = LoggerFactory.getLogger(PointDTO.class);

    private Integer id;
    private Integer functionId;
    private double x;
    private double y;

    public PointDTO(Integer id, Integer functionId, double x, double y) {
        log.debug("[PointDTO] ctor(id={}, functionId={}, x={}, y={})",
                id, functionId, x, y);

        this.id = id;
        this.functionId = functionId;
        this.x = x;
        this.y = y;
    }

    public PointDTO(Integer functionId, double x, double y) {
        this(null, functionId, x, y);
    }

    public Integer getId() {
        log.trace("[PointDTO] getId() -> {}", id);
        return id;
    }

    public void setId(Integer id) {
        log.debug("[PointDTO] setId({}) old={}", id, this.id);
        this.id = id;
    }

    public Integer getFunctionId() {
        log.trace("[PointDTO] getFunctionId() -> {}", functionId);
        return functionId;
    }

    public void setFunctionId(Integer functionId) {
        log.debug("[PointDTO] setFunctionId({}) old={}", functionId, this.functionId);
        this.functionId = functionId;
    }

    public double getX() {
        log.trace("[PointDTO] getX() -> {}", x);
        return x;
    }

    public void setX(double x) {
        log.debug("[PointDTO] setX({}) old={}", x, this.x);
        this.x = x;
    }

    public double getY() {
        log.trace("[PointDTO] getY() -> {}", y);
        return y;
    }

    public void setY(double y) {
        log.debug("[PointDTO] setY({}) old={}", y, this.y);
        this.y = y;
    }

    @Override
    public String toString() {
        String s = "PointDTO{" +
                "id=" + id +
                ", functionId=" + functionId +
                ", x=" + x +
                ", y=" + y +
                '}';
        log.trace("[PointDTO] toString() -> {}", s);
        return s;
    }
}
