package com.orainge.wenwen.service.impl;

import com.orainge.wenwen.service.AuthService;
import com.orainge.wenwen.shiro.util.PasswordUtil;
import com.orainge.wenwen.shiro.util.RegisterUtil;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private RegisterUtil registerUtil;
    @Autowired
    private PasswordUtil passwordUtil;

    /**
     * API: 用户提交注册信息进行注册
     */
    @Override
    public Response apiRegister(String email, String username, String password) {
        return registerUtil.register(email, username, password);
    }

    /**
     * API: 发送激活邮件到指定邮箱
     */
    @Override
    public Response apiSendActivate(String email) {
        return registerUtil.sendActivateEmail(email);
    }

    /**
     * API: 给指定邮箱发送重置密码邮件
     */
    @Override
    public Response apiSendReset(String email) {
        return passwordUtil.sendResetPasswordEmail(email);
    }

    /**
     * API: 激活指定邮箱的账户
     */
    @Override
    public Response apiActivate(String token) {
        return registerUtil.activateByToken(token);
    }

    /**
     * 页面：根据 Token 来检查是否有权限重置密码
     */
    @Override
    public Response toResetPassword(String token) {
        return passwordUtil.authTokenValid(token);
    }

    /**
     * API: 根据 Token 重置密码
     */
    @Override
    public Response apiResetPassword(String token, String password) {
        return passwordUtil.resetPasswordByToken(token, password);
    }
}
