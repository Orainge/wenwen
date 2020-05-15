package com.orainge.wenwen.exception;

public class NullRequestParametersException extends DefaultException {
    {
        from = "请求参数";
        message = from + "错误";
    }

    public NullRequestParametersException() {
        super();
    }

    public NullRequestParametersException(String... parameters) {
        super();
        setMessage(parameters);
    }

    private void setMessage(String... parameters) {
        String m = "参数错误，请求参数 [";
        for (int i = 0; i < parameters.length; i++) {
            m += parameters[i];
            if (i != parameters.length - 1) {
                m += ", ";
            }
        }
        m += "] 不能为空";
        this.message = m;
    }

    public String getMessage() {
        return this.message;
    }
}