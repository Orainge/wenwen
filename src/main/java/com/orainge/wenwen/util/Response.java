package com.orainge.wenwen.util;

/**
 * 封装的返回数据
 */
public class Response {
    private Integer code;
    private String message;
    private Object data;

    public Response() {
        code = 0;
        message = null;
        data = null;
    }

    public Response(Integer code) {
        this.code = code;
    }

    public Response(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}