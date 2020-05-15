package com.orainge.wenwen;

import com.orainge.wenwen.exception.*;
import com.orainge.wenwen.util.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

//全局异常捕捉处理
@ControllerAdvice
public class WebExceptionHandler {
    private String errorPage500 = "/error/500";
    private String errorPage404 = "/error/404";

    /* URL 请求中缺少参数 默认处理器（页面级别） */
    @ExceptionHandler(value = NullRequestParametersException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String nullRequestParametersExceptionHandler(NullRequestParametersException e) {
        System.err.println("URL 请求中缺少参数 默认处理器（页面级别）");
        e.printStackTrace();
        return errorPage404;
    }

    /* 路径中的参数非法（页面级别） */
    @ExceptionHandler(value = InvalidVariableException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String nvalidVariableExceptionHandler(InvalidVariableException e) {
//        System.err.println("路径中的参数非法（页面级别）");
//        e.printStackTrace();
        return errorPage404;
    }

    /* 文件上传错误 处理器（API级别） */
    @ResponseBody
    @ExceptionHandler(value = FileUploadException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Response fileUploadExceptionHandler(FileUploadException e) {
        System.err.println("文件上传错误 处理器（API级别）");
        e.printStackTrace();
        return new Response(1, "服务器错误，请稍后再试");
    }

    /* 自定义异常类默认处理器（API级别） */
    @ResponseBody
    @ExceptionHandler(value = DefaultException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Response defaultExceptionHandler(DefaultException e) {
        System.err.println("自定义异常类默认处理器（API级别）");
        e.printStackTrace();
        return new Response(1, "服务器错误，请稍后再试");
    }

    /* 异常类默认处理器（API级别） */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Response errorHandler(Exception e) {
        System.err.println("异常类默认处理器（API级别）");
        e.printStackTrace();
        return new Response(1, "服务器错误，请稍后再试");
    }

    /* 任意资源未找到（页面级别） */
    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String notFoundExceptionHandler(NotFoundException e) {
//        System.err.println("任意资源未找到（页面级别）");
//        e.printStackTrace();
        return errorPage404;
    }
}