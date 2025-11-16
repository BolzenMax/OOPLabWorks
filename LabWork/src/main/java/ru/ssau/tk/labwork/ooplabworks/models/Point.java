package ru.ssau.tk.labwork.ooplabworks.models;

public class Point {
    private Integer id;
    private Integer functionId;
    private double x;
    private double y;

    public Point() {}

    public Point(Integer id, Integer functionId, double x, double y) {
        this.id = id;
        this.functionId = functionId;
        this.x = x;
        this.y = y;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getFunctionId() { return functionId; }
    public void setFunctionId(Integer functionId) { this.functionId = functionId; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
}
