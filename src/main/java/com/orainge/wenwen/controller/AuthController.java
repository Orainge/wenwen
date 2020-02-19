package com.orainge.wenwen.controller;

import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.service.AuthService;
import com.orainge.wenwen.util.Response;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;
    private String[] authPath = new String[]{"/login", "logout"};

    @RequestMapping("/login")
    public String toLogin(HttpServletRequest request) {
        if (authService.isAuthenticated()) {
            return "redirect:/"; // 授权了就直接访问主页
        } else {
            return "login";
        }
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public Response toAuth(String principal, String password, HttpServletRequest request) {
        boolean rememberMe = ServletRequestUtils.getBooleanParameter(request, "rememberMe", false);
        Response response = authService.auth(principal, password, rememberMe);
        if (response.getCode() == 0) {
            String url = authService.getBeforeLoginRedirectUrl(request);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("redirectUrl", url);
            // TODO  放入用户信息 data.put("userInfo", user);
            response.setData(data);
        }
        return response;
    }

    @RequestMapping("/logout")
    public String toLogout() {
        Response response = authService.logout();
        return "redirect:/login";
    }
}
