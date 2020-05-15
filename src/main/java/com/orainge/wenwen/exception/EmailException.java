package com.orainge.wenwen.exception;

import com.orainge.wenwen.exception.type.Error;

public class EmailException extends DefaultException {
    private String from = "邮件模块";
    private String message = from + "发生错误";

    public EmailException() {
        super();
    }

    public EmailException(Exception e) {
        super();
        processException(e);
    }

    public EmailException(String message) {
        this.message = this.message + ": [" + message + "]";
    }

    public EmailException(Exception exception, String message) {
        super();
        this.message = this.message + ": [" + message + "]";
        processException(exception);
    }

    public EmailException(Error error) {
        super();
        setMessage(error);
    }

    public EmailException(Exception exception, Error error) {
        super();
        setMessage(error);
        processException(exception);
    }

    public EmailException(Error error, String message) {
        super();
        setMessage(error, message);
    }

    public EmailException(Exception exception, Error error, String message) {
        super();
        setMessage(error, message);
        processException(exception);
    }

    private void setMessage(Error error) {
        String m = from + "发生 ";
        m += error.getType();
        m += " 错误";
        this.message = m;
    }

    private void setMessage(Error error, String message) {
        String m = from + "发生 ";
        m += error.getType();
        m += " 错误: [" + message + "]";
        this.message = m;
    }

    private void processException(Exception e) {
        System.err.println("原始错误如下:");
        e.printStackTrace();
    }

    public String getMessage() {
        return this.message;
    }
}
