package ru.ssau.tk.labwork.ooplabworks.dto;

public class FunctionDTO {
    private Long id;
    private Long userId;
    private String name;
    private String signature;

    public FunctionDTO() {}

    public FunctionDTO(Long id, Long userId, String name, String signature) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.signature = signature;
    }

    public FunctionDTO(Long userId, String name, String signature) {
        this.userId = userId;
        this.name = name;
        this.signature = signature;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
}