package com.orainge.wenwen.exception;

import com.orainge.wenwen.exception.DefaultException;

public class FileUploadException extends DefaultException {
    private String from = "文件上传";
    private String message = from + "发生错误";

    public FileUploadException() {
        super();
    }

    public FileUploadException(Exception e) {
        super();
        processException(e);
    }

    public FileUploadException(String message) {
        this.message = this.message + ": [" + message + "]";
    }

    public FileUploadException(Exception exception, String message) {
        super();
        this.message = this.message + ": [" + message + "]";
        processException(exception);
    }

    private void processException(Exception e) {
        System.err.println("原始错误如下:");
        e.printStackTrace();
    }

    public String getMessage() {
        return this.message;
    }
}
