package com.orainge.wenwen.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(DefaultException.class);
    protected String from = "";
    protected String message = "默认错误";

    public DefaultException() {
        super();
    }

    public DefaultException(String message) {
        super(message);
    }

    protected void processException(Exception e) {
        logger.error(e.getMessage(), e);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}