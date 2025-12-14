package ru.ssau.tk.labwork.ooplabworks.ui.dto;

import java.util.List;

public class TabulatedFunctionPayload {
    private List<UiPoint> points;
    private String name;

    public TabulatedFunctionPayload() {
    }

    public TabulatedFunctionPayload(List<UiPoint> points) {
        this.points = points;
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