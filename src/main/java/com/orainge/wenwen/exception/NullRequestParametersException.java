package com.orainge.wenwen.exception;

public class NullRequestParametersException extends DefaultException {
    private String from = "请求参数";
    private String message = from + "错误";

    public NullRequestParametersException() {
        super();
    }

    public NullRequestParametersException(Exception exception) {
        super();
        processException(exception);
    }

    public NullRequestParametersException(String... parameters) {
        super();
        setMessage(parameters);
    }

    public NullRequestParametersException(Exception exception, String... parameters) {
        super();
        setMessage(parameters);
        processException(exception);
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

    private void processException(Exception e) {
        System.err.println("原始错误如下:");
        e.printStackTrace();
    }

    public String getMessage() {
        return this.message;
    }
}
