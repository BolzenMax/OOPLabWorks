package ru.ssau.tk.labwork.ooplabworks.dto;

public class PointRequest {
    private Long functionId;
    private Double x;
    private Double y;

    public PointRequest() {}

    public PointRequest(Long functionId, Double x, Double y) {
        this.functionId = functionId;
        this.x = x;
        this.y = y;
    }

    public Long getFunctionId() { return functionId; }
    public void setFunctionId(Long functionId) { this.functionId = functionId; }

    public Double getX() { return x; }
    public void setX(Double x) { this.x = x; }

    public Double getY() { return y; }
    public void setY(Double y) { this.y = y; }
}