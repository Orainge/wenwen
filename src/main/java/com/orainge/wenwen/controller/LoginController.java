package com.orainge.wenwen.controller;

import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.service.LoginService;
import com.orainge.wenwen.exception.NullRequestParametersException;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * 页面: 用户登录页面
     */
    @GetMapping(value = "/login")
    public String toLogin(HttpServletRequest request) {
        if (loginService.isAuthenticated()) {
            return "redirect:/"; // 授权了就直接访问主页
        } else {
            return "auth/login";
        }
    }

    /**
     * API: 用户退出登录
     */
    @GetMapping(value = "/logout")
    public String apiLogout() {
        Response response = loginService.apiLogout();
        return "redirect:/login";
    }

    /**
     * API: 验证用户登录信息
     */
    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiLogin(@RequestBody(required = false) Map<String, Object> map, HttpServletRequest request) throws NullRequestParametersException {
        Object[] result = ControllerHelper.getParametersInObject(map, "principal", "password", "rememberMe");
        Response response = loginService.apiLogin(result[0].toString(), result[1].toString(), (Integer) result[2], request);
        return response;
    }
}