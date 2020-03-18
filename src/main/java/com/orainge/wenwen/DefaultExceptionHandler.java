package com.orainge.wenwen;

import com.orainge.wenwen.controller.util.NullParametersException;
import com.orainge.wenwen.util.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//全局异常捕捉处理
@ControllerAdvice
public class DefaultExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response errorHandler(Exception e) {
        e.printStackTrace();
        return new Response(400, "服务器错误，请稍后再试", null);
    }

    @ResponseBody
    @ExceptionHandler(value = NullParametersException.class)
    public Response errorNullParmatersException(NullParametersException e) {
        e.printStackTrace();
        return new Response(400, "服务器错误，请稍后再试", null);
    }
}
