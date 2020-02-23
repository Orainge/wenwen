package com.orainge.wenwen;

import com.orainge.wenwen.util.NullParmatersException;
import com.orainge.wenwen.util.Response;
import com.sun.xml.internal.ws.client.ResponseContext;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

//全局异常捕捉处理
@ControllerAdvice
public class DefaultExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response errorHandler(Exception e) {
        e.printStackTrace();
        return new Response(400, "服务器错误，请稍后再试", null);
    }

    @ExceptionHandler(value = NullParmatersException.class)
    public Response errorNullParmatersException(NullParmatersException e) {
        e.printStackTrace();
        return new Response(400, "服务器错误，请稍后再试", null);
    }
}
