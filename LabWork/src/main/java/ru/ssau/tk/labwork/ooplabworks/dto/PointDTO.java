package ru.ssau.tk.labwork.ooplabworks.dto;

public class PointDTO {
    private Long id;
    private Long functionId;
    private Double x;
    private Double y;

    public PointDTO() {}

    public PointDTO(Long id, Long functionId, Double x, Double y) {
        this.id = id;
        this.functionId = functionId;
        this.x = x;
        this.y = y;
    }

    public PointDTO(Long functionId, Double x, Double y) {
        this.functionId = functionId;
        this.x = x;
        this.y = y;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFunctionId() { return functionId; }
    public void setFunctionId(Long functionId) { this.functionId = functionId; }

    public Double getX() { return x; }
    public void setX(Double x) { this.x = x; }

    public Double getY() { return y; }
    public void setY(Double y) { this.y = y; }
}