package com.orainge.wenwen.exception;

public class DefaultException extends RuntimeException {
    private String message = "默认错误";

    public DefaultException() {
        super();
    }

    public DefaultException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}