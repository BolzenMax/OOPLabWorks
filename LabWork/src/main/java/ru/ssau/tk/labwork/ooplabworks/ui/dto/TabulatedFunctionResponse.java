package ru.ssau.tk.labwork.ooplabworks.ui.dto;

import java.util.List;

public class TabulatedFunctionResponse {
    private List<UiPoint> points;
    private double leftBound;
    private double rightBound;
    private boolean insertable;
    private boolean removable;
    private String name;

    public TabulatedFunctionResponse() {
    }

    public TabulatedFunctionResponse(List<UiPoint> points, double leftBound, double rightBound, boolean insertable, boolean removable, String name) {
        this.points = points;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.insertable = insertable;
        this.removable = removable;
        this.name = name;
    }

    public List<UiPoint> getPoints() {
        return points;
    }

    public void setPoints(List<UiPoint> points) {
        this.points = points;
    }

    public double getLeftBound() {
        return leftBound;
    }

    public void setLeftBound(double leftBound) {
        this.leftBound = leftBound;
    }

    public double getRightBound() {
        return rightBound;
    }

    public void setRightBound(double rightBound) {
        this.rightBound = rightBound;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}