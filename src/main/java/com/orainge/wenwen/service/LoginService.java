package com.orainge.wenwen.service;

import com.orainge.wenwen.util.Response;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    /**
     * 执行 登录 操作
     *
     * @param principal  用户名或邮箱
     * @param password   密码
     * @param rememberMe 是否勾选” "记住我"
     * @param request HttpServletRequest
     * @return 登录信息{"code": 登录状态码, "message"： 登录状态码提示}
     */
    public Response apiLogin(String principal, String password, Integer rememberMe, HttpServletRequest request);


    /**
     * 执行 退出登录 操作
     *
     * @return 退出登录信息{"code": 退出登录码, "message"： 退出登录提示}
     */
    public Response apiLogout();

    /**
     * 检查是否已经有用户登录了
     *
     * @return 用户登陆状态
     */
    public boolean isAuthenticated();
}