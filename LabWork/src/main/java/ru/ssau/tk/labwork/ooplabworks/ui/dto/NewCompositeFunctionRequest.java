package ru.ssau.tk.labwork.ooplabworks.ui.dto;

public class NewCompositeFunctionRequest {
    private String displayName;
    private String outerFunction;
    private String innerFunction;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOuterFunction() {
        return outerFunction;
    }

    public void setOuterFunction(String outerFunction) {
        this.outerFunction = outerFunction;
    }

    public String getInnerFunction() {
        return innerFunction;
    }

    public void setInnerFunction(String innerFunction) {
        this.innerFunction = innerFunction;
    }
}