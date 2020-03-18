package com.orainge.wenwen.controller.util;

public class NullParametersException extends Exception {
    private String message = "请求参数不能为空";

    public NullParametersException() {
        super();
    }

    public NullParametersException(String message) {
        super();
        setMessage(message);
    }

    public NullParametersException(boolean isParameters, String... str) {
        super();
        if (str.length != 0) {
            if (isParameters) {
                setMessageByParameters(str);
            } else {
                this.message = str[0];
            }
        }
    }

    public void setMessageByParameters(String... parameters) {
        if (parameters.length != 0) {
            this.message = "请求参数 ";
            for (String parameter : parameters) {
                message += parameter + " ";
            }
            this.message += "为空";
        } else {

        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
