package com.orainge.wenwen.exception;

import com.orainge.wenwen.exception.type.Error;

public class MongoDBException extends DefaultException {
    {
        from = "MongoDB 数据库";
        message = from + "发生错误";
    }

    public MongoDBException(Exception exception, Error error, String message) {
        super();
        setMessage(error, message);
        processException(exception);
    }

    private void setMessage(Error error, String message) {
        String m = from + "发生 ";
        m += error.getType();
        m += " 错误: [" + message + "]";
        this.message = m;
    }

    public String getMessage() {
        return this.message;
    }
}
