package com.orainge.wenwen.exception;

public class InvalidVariableException extends DefaultException {
    {
        message = "路径中的参数非法";
    }

    public InvalidVariableException(String... variables) {
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
