package ru.ssau.tk.labwork.ooplabworks.ui.dto;

public class FactorySelectionRequest {
    private String type;

    public FactorySelectionRequest() {
    }

    public FactorySelectionRequest(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}