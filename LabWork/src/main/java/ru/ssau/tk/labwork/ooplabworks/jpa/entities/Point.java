package ru.ssau.tk.labwork.ooplabworks.jpa.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Points")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "function_id", nullable = false)
    private Long functionId;

    @Column(name = "x_value", nullable = false)
    private Double x;

    @Column(name = "y_value", nullable = false)
    private Double y;

    public Point() {}

    public Point(Long functionId, Double x, Double y) {
        this.functionId = functionId;
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}