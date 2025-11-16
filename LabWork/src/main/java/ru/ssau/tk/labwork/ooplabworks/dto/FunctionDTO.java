package ru.ssau.tk.labwork.ooplabworks.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionDTO {

    private static final Logger log = LoggerFactory.getLogger(FunctionDTO.class);

    private Integer id;
    private Integer userId;
    private String name;
    private String signature;

    public FunctionDTO(Integer id, Integer userId, String name, String signature) {
        log.debug("[FunctionDTO] ctor(id={}, userId={}, name={}, signature={})",
                id, userId, name, signature);

        this.id = id;
        this.userId = userId;
        this.name = name;
        this.signature = signature;
    }

    public FunctionDTO(Integer userId, String name, String signature) {
        this(null, userId, name, signature);
    }

    public Integer getId() {
        log.trace("[FunctionDTO] getId() -> {}", id);
        return id;
    }

    public void setId(Integer id) {
        log.debug("[FunctionDTO] setId({}) old={}", id, this.id);
        this.id = id;
    }

    public Integer getUserId() {
        log.trace("[FunctionDTO] getUserId() -> {}", userId);
        return userId;
    }

    public void setUserId(Integer userId) {
        log.debug("[FunctionDTO] setUserId({}) old={}", userId, this.userId);
        this.userId = userId;
    }

    public String getName() {
        log.trace("[FunctionDTO] getName() -> {}", name);
        return name;
    }

    public void setName(String name) {
        log.debug("[FunctionDTO] setName({}) old={}", name, this.name);
        this.name = name;
    }

    public String getSignature() {
        log.trace("[FunctionDTO] getSignature() -> {}", signature);
        return signature;
    }

    public void setSignature(String signature) {
        log.debug("[FunctionDTO] setSignature({}) old={}", signature, this.signature);
        this.signature = signature;
    }

    @Override
    public String toString() {
        String s = "FunctionDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", signature='" + signature + '\'' +
                '}';
        log.trace("[FunctionDTO] toString() -> {}", s);
        return s;
    }
}
