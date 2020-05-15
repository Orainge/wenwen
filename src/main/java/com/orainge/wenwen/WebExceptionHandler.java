package com.orainge.wenwen;

import com.orainge.wenwen.exception.*;
import com.orainge.wenwen.redis.util.RedisTimer;
import com.orainge.wenwen.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

//全局异常捕捉处理
@ControllerAdvice
public class WebExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebExceptionHandler.class);
    private final Response errorResponse = new Response(1,"服务器错误，请稍后再试");
    private final String errorPage500 = "/error/500";
    private final String errorPage404 = "/error/404";

    /* URL 请求中缺少参数 默认处理器（页面） */
    @ExceptionHandler(value = NullRequestParametersException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String nullRequestParametersExceptionHandler(NullRequestParametersException e) {
        logger.info("页面 - URL 请求中缺少参数", e);
        return errorPage404;
    }

    /* 路径中的参数非法（页面） */
    @ExceptionHandler(value = InvalidVariableException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String invalidVariableExceptionHandler(InvalidVariableException e) {
        logger.info("页面 - 路径中的参数非法", e);
        return errorPage404;
    }

    /* 文件上传错误（API） */
    @ResponseBody
    @ExceptionHandler(value = FileUploadException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Response fileUploadExceptionHandler(FileUploadException e) {
        logger.info("API - 文件上传错误", e);
        return errorResponse;
    }

    /* 自定义异常类默认处理器（API） */
    @ResponseBody
    @ExceptionHandler(value = DefaultException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public String defaultExceptionHandler(DefaultException e) {
        logger.info("自定义异常类默认处理", e);
        return errorPage500;
    }

    /* 异常类默认处理器（页面） */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public String errorHandler(Exception e) {
        logger.info("异常默认处理", e);
        return errorPage500;
    }

    /* 任意资源未找到（页面） */
    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String notFoundExceptionHandler(NotFoundException e) {
        return errorPage404;
    }
}