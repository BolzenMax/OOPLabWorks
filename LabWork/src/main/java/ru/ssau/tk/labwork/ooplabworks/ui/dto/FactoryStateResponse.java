package ru.ssau.tk.labwork.ooplabworks.ui.dto;

public class FactoryStateResponse {
    private String type;
    private String displayName;

    public FactoryStateResponse() {
    }

    public FactoryStateResponse(String type, String displayName) {
        this.type = type;
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}