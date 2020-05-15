package com.orainge.wenwen.controller.util;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ErrorPageController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String error(HttpServletRequest request) {
        return "error/404";
    }

    @RequestMapping(value = "/error/{code}")
    public String errorPage(@PathVariable(value = "code") int code, HttpServletResponse response) {
        String page = "error/";
        switch (code) {
            case 403:
                response.setStatus(403);
                page += "403";
                break;
            case 404:
                response.setStatus(404);
                page += "404";
                break;
            case 500:
                response.setStatus(500);
                page += "500";
                break;
            default:
                response.setStatus(404);
                page += "404";
                break;
        }
        return page;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
