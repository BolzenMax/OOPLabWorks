package ru.ssau.tk.labwork.ooplabworks.models;

public class Function {
    private Integer id;
    private Integer userId;
    private String name;
    private String signature;

    public Function() {}

    public Function(Integer id, Integer userId, String name, String signature) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.signature = signature;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
}
