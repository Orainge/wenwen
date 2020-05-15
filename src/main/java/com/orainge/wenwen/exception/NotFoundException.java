package com.orainge.wenwen.exception;

import com.orainge.wenwen.exception.type.Error;

public class NotFoundException extends DefaultException {
    {
        message = "资源未找到";
    }

    public NotFoundException() {
        super();
    }

    public NotFoundException(String... variables) {
        super();
        String v = "";
        for (int i = 0; i < variables.length; i++) {
            v += variables[i];
            if (i + 1 != variables.length) {
                v += ", ";
            }
        }
        this.message = this.message + ": [" + v + "]";
    }

    public String getMessage() {
        return this.message;
    }
}
