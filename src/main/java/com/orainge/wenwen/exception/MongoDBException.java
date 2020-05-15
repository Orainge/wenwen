package com.orainge.wenwen.exception;

import com.orainge.wenwen.exception.type.Error;

public class MongoDBException extends DefaultException {
    private String from = "MongoDB 数据库";
    private String message = from + "发生错误";

    public MongoDBException() {
        super();
    }

    public MongoDBException(Exception e) {
        super();
        processException(e);
    }

    public MongoDBException(String message) {
        this.message = this.message + ": [" + message + "]";
    }

    public MongoDBException(Exception exception, String message) {
        super();
        this.message = this.message + ": [" + message + "]";
        processException(exception);
    }

    public MongoDBException(Error error) {
        super();
        setMessage(error);
    }

    public MongoDBException(Exception exception, Error error) {
        super();
        setMessage(error);
        processException(exception);
    }

    public MongoDBException(Error error, String message) {
        super();
        setMessage(error, message);
    }

    public MongoDBException(Exception exception, Error error, String message) {
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
