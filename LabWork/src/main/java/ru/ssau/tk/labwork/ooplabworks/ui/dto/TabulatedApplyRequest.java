package ru.ssau.tk.labwork.ooplabworks.ui.dto;

public class TabulatedApplyRequest {
    private TabulatedFunctionPayload payload;
    private String x;

    public TabulatedFunctionPayload getPayload() {
        return payload;
    }

    public void setPayload(TabulatedFunctionPayload payload) {
        this.payload = payload;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }
}