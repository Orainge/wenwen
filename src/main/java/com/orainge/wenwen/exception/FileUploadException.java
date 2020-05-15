package com.orainge.wenwen.exception;

import com.orainge.wenwen.exception.DefaultException;

public class FileUploadException extends DefaultException {
    {
        from = "文件上传";
        message = from + "发生错误";
    }

    public FileUploadException() {
        super();
    }

    public String getMessage() {
        return this.message;
    }
}
