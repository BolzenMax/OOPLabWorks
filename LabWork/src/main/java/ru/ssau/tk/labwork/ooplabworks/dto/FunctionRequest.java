package ru.ssau.tk.labwork.ooplabworks.dto;

public class FunctionRequest {
    private Long userId;
    private String name;
    private String signature;

    public FunctionRequest() {}

    public FunctionRequest(Long userId, String name, String signature) {
        this.userId = userId;
        this.name = name;
        this.signature = signature;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
}