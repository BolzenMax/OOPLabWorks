package ru.ssau.tk.labwork.ooplabworks.ui.dto;

public class DerivativeRequest {
    private TabulatedFunctionPayload function;
    private String resultName;

    public DerivativeRequest() {
    }

    public TabulatedFunctionPayload getFunction() {
        return function;
    }

    public void setFunction(TabulatedFunctionPayload function) {
        this.function = function;
    }

    public String getResultName() {
        return resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }
}