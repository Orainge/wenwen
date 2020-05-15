package com.orainge.wenwen.exception;

import com.orainge.wenwen.exception.type.Error;

public class NotFoundException extends DefaultException {
    private String message = "资源未找到";

    public NotFoundException() {
        super();
    }

    public NotFoundException(Exception e) {
        super();
        processException(e);
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

    public NotFoundException(Exception exception, String... variables) {
        super();
        String v = "";
        for (int i = 0; i < variables.length; i++) {
            v += variables[i];
            if (i + 1 != variables.length) {
                v += ", ";
            }
        }
        this.message = this.message + ": [" + v + "]";
        processException(exception);
    }

    private void processException(Exception e) {
//        System.err.println("原始错误如下:");
//        e.printStackTrace();
    }

    public String getMessage() {
        return this.message;
    }
}
