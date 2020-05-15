package com.orainge.wenwen.exception.type;

public enum EmailError implements Error {
    SEND(0, "发送");

    private Integer code;
    private String type;

    private EmailError(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getType() {
        return type;
    }
}
