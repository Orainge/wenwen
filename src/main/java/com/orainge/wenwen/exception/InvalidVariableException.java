package com.orainge.wenwen.exception;

public class InvalidVariableException extends DefaultException {
    private String message = "路径中的参数非法";

    public InvalidVariableException() {
        super();
    }

    public InvalidVariableException(Exception e) {
        super();
        processException(e);
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

    public InvalidVariableException(Exception exception, String... variables) {
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
