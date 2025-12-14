package ru.ssau.tk.labwork.ooplabworks.ui.dto;

public class MathFunctionOption {
    private String key;
    private String displayName;

    public MathFunctionOption() {
    }

    public MathFunctionOption(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}