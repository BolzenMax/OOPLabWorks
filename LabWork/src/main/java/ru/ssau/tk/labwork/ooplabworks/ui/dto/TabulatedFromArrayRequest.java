package ru.ssau.tk.labwork.ooplabworks.ui.dto;

import java.util.List;

import ru.ssau.tk.labwork.ooplabworks.ui.dto.UiPoint;

public class TabulatedFromArrayRequest {
    private Integer pointsCount;
    private List<String> xValues;
    private List<String> yValues;
    private List<UiPoint> points;
    private String name;

    public Integer getPointsCount() {
        return pointsCount;
    }

    public void setPointsCount(Integer pointsCount) {
        this.pointsCount = pointsCount;
    }

    public List<String> getXValues() {
        return xValues;
    }

    public void setXValues(List<String> xValues) {
        this.xValues = xValues;
    }

    public List<String> getYValues() {
        return yValues;
    }

    public void setYValues(List<String> yValues) {
        this.yValues = yValues;
    }

    public List<UiPoint> getPoints() {
        return points;
    }

    public void setPoints(List<UiPoint> points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}