package com.orainge.wenwen.service.impl;

import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.mybatis.entity.User;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import com.orainge.wenwen.service.AuthService;
import com.orainge.wenwen.shiro.AuthUtil;
import com.orainge.wenwen.util.Response;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Response auth(String principal, String password, boolean rememberMe, String... paramters) {
        // TODO 在日志管理器输出登录结果，具体写入登陆日期、登录时间等等。
        int code = authUtil.login(principal, password, rememberMe);
        String message = authUtil.getLoginMessage(code);
        if (code == 0) {
            /* 登录成功，放入用户信息 */
            User user = userMapper.getUserInfoByPricipal(principal);
            return new Response(code, message, user);
        }
        return new Response(code, message);
    }

    @Override
    public Response logout(String... paramters) {
        // TODO 在日志管理器输出登录结果，具体写入登陆日期、登录时间等等。
        int code = authUtil.logout();
        String message = authUtil.getLogoutMessage(code);
        return new Response(code, message);
    }

    @Override
    public boolean isAuthenticated() {
        return authUtil.isAuthenticated();
    }

    @Override
    public String getBeforeLoginRedirectUrl(HttpServletRequest request) {
        SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
        String url = "/";
        if (savedRequest != null) {
            url = savedRequest.getRequestUrl();
            if (url.equals("/login") || url.equals("/logout")) {
                return "/";
            } else
                return url;
        }
        return url;
    }
}