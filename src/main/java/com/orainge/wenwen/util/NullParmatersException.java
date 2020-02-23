package com.orainge.wenwen.util;

public class NullParmatersException extends Exception {
    private final String defaultMessage = "请求参数不能为空";
    private String message;

    public NullParmatersException() {
        super();
        this.message = defaultMessage;
    }

    public NullParmatersException(boolean isParmatersName, String... parmateresName) {
        super();
        if (isParmatersName) {
            setMessage(parmateresName);
        } else {
            this.message = parmateresName[0];
        }
    }

    public NullParmatersException(Exception e, String... parmateresName) {
        super();
        e.printStackTrace();
        setMessage(parmateresName);
    }

    public NullParmatersException(Exception e) {
        super();
        e.printStackTrace();
    }

    public NullParmatersException(String message) {
        super();
        this.message = message;
    }

    private void setMessage(String... parmateresName) {
        if (parmateresName.length == 0) {
            this.message = defaultMessage;
        } else {
            this.message = "请求参数 ";
            for (String parmaters : parmateresName) {
                message += parmaters + " ";
            }
            this.message += "为空";
        }
    }

    public String getMessage() {
        return this.message;
    }
}
