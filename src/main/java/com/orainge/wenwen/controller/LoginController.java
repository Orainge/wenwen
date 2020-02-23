package com.orainge.wenwen.controller;

import com.orainge.wenwen.service.LoginService;
import com.orainge.wenwen.util.NullParmatersException;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * 页面: 用户登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
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
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String apiLogout() {
        Response response = loginService.apiLogout();
        return "redirect:/login";
    }

    /**
     * API: 验证用户登录信息
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiLogin(@RequestBody(required = false) Map<String, Object> map, HttpServletRequest request) throws NullParmatersException {
        Object[] result = ControllerHelper.getMapParamtersInObject(map, "principal", "password", "rememberMe");
        return loginService.apiLogin(result[0].toString(), result[1].toString(), (Integer) result[2], request);
    }
}