package com.orainge.wenwen.exception;

import com.orainge.wenwen.exception.type.Error;

public class EmailException extends DefaultException {
    {
        from = "邮件模块";
        message = from + "发生错误";
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

    public EmailException(Exception exception, String message) {
        super();
        this.message = this.message + ": [" + message + "]";
        processException(exception);
    }

    private void setMessage(Error error) {
        String m = from + "发生 ";
        m += error.getType();
        m += " 错误";
        this.message = m;
    }

    public String getMessage() {
        return this.message;
    }
}
