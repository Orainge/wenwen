package com.orainge.wenwen.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.mybatis.entity.User;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import com.orainge.wenwen.service.LoginService;
import com.orainge.wenwen.shiro.util.LoginUtil;
import com.orainge.wenwen.util.Response;
import com.orainge.wenwen.util.WebsiteSettings;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginUtil loginUtil;
    @Autowired
    private UserMapper userMapper;


    @Override
    public Response apiLogin(String principal, String password, Integer rememberMe, HttpServletRequest request) {
        Response response = loginUtil.login(principal, password, rememberMe == 1);
        if (response.getCode() == 0) {
            try {
                /* 登录成功，放入用户信息 */
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("redirectUrl", getRedirectUrl(request));
                data.put("userInfo", userMapper.getUserInfoByPricipal(principal));
                response.setData(data);
                // 更新登录时间
                User userTime = new User();
                userTime.setUsername(principal);
                userTime.setEmail(principal);
                userTime.setLastLoginTime(new Date());
                if (userMapper.updateLoginTime(userTime) != 1) {
                    throw new Exception("更新登录时间出现错误");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new Response(1, "服务器错误，请稍后再试");
            }
        }
        return response;
    }

    @Override
    public Response apiLogout() {
        return loginUtil.logout();
    }

    @Override
    public boolean isAuthenticated() {
        return loginUtil.isAuthenticated();
    }

    /**
     * 获得登陆前的页面，以便登录后跳转
     */
    private String getRedirectUrl(HttpServletRequest request) {
        SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
        String url = "/";
        if (savedRequest != null) {
            url = savedRequest.getRequestUrl();
            for (String str : WebsiteSettings.TO_REDIRECT_INDEX_URL) {
                if (StringUtils.equals(str, url)) {
                    break;
                }
            }
        }
        return url;
    }
}