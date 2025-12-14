package ru.ssau.tk.labwork.ooplabworks.ui.dto;

public class UiPoint {
    private double x;
    private double y;

    public UiPoint() {
    }

    public UiPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}