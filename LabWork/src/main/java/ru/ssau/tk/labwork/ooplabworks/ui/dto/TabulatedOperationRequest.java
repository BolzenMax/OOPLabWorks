package ru.ssau.tk.labwork.ooplabworks.ui.dto;

public class TabulatedOperationRequest {
    private String operation;
    private TabulatedFunctionPayload first;
    private TabulatedFunctionPayload second;
    private String resultName;

    public TabulatedOperationRequest() {
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public TabulatedFunctionPayload getFirst() {
        return first;
    }

    public void setFirst(TabulatedFunctionPayload first) {
        this.first = first;
    }

    public TabulatedFunctionPayload getSecond() {
        return second;
    }

    public void setSecond(TabulatedFunctionPayload second) {
        this.second = second;
    }

    public String getResultName() {
        return resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }
}