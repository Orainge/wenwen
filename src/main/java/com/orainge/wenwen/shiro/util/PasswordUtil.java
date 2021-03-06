package com.orainge.wenwen.shiro.util;

import com.alibaba.druid.util.StringUtils;
import com.orainge.wenwen.mybatis.entity.User;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import com.orainge.wenwen.redis.util.RedisPrefix;
import com.orainge.wenwen.redis.util.RedisUtil;
import com.orainge.wenwen.util.EmailUtil;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PasswordUtil {
    @Autowired
    UserMapper userMapper;
    @Autowired
    EmailUtil emailUtil;
    @Autowired
    RedisUtil redisUtil;

    // 错误提示 状态码对应数组的下标
    private final String[] sendResetPasswordMessage = {"密码重置链接发送成功，请注意查收", "连接错误，请稍后再试", "用户不存在", "用户还未激活，请先激活后再试"};
    private final String[] tokenCheckMessage = {"重置密码链接有效", "连接错误，请稍后再试", "重置密码链接无效"};
    private final String[] resetPasswordMessage = {"密码修改成功", "连接错误，请稍后再试", "重置密码链接已过期，请重新申请", "旧密码错误"};
    private final String resetPasswordPrefix = RedisPrefix.EMAIL_RESET_PASSWORD;

    /**
     * 发送重置密码邮件
     */
    public Response sendResetPasswordEmail(String email) {
        int code = 0;
        User user = userMapper.getUserStatus(email);
        if (user == null) {
            // 说明不存在这个用户
            code = 2;
        } else if (user.getUserStatus() == 2) {
            // 用户还未激活
            code = 3;
        } else {
            // 存在这个用户，生成token
            String[] tokens = CommonUtil.getToken(email);
            String encodeEmail = tokens[0];
            String originalToken = tokens[1];
            String encodeToken = tokens[2];
            String redisKey = resetPasswordPrefix + encodeEmail;

            Map<String, String> hPuts = new HashMap<String, String>();
            hPuts.put("token", originalToken);
            hPuts.put("email", email);
            redisUtil.hset(redisKey, hPuts, 10 * 60);
            emailUtil.sendResetPasswordEmail(email, encodeToken);
        }
        return new Response(code, sendResetPasswordMessage[code]);
    }

    /**
     * 验证 Token 是否有效
     */
    public Response authTokenValid(String encodeToken) {
        int code = 0;
        String email = null;
        Map<String, String> data = null;
        String[] tokens = CommonUtil.verifyToken(encodeToken);
        if (tokens == null) {
            //token 格式无效
            code = 2;
        } else {
            String encodeEmail = tokens[0];
            String originalToken = tokens[1];
            String redisKey = resetPasswordPrefix + encodeEmail;

            Map<String, String> redisTokenObj = redisUtil.hgetAll(redisKey);
            if (redisTokenObj == null || redisTokenObj.isEmpty()) {
                // 说明这个账户没申请过重置密码链接
                code = 2;
            } else {
                // 说明这个账户申请过重置密码链接
                email = redisTokenObj.get("email");
                String redisToken = redisTokenObj.get("token");
                if (!StringUtils.equals(redisToken, originalToken)) {
                    // token错误，激活链接无效
                    code = 2;
                } else {
                    // 相等，可以重置密码
                    data = new HashMap<String, String>();
                    data.put("token", encodeToken);
                    data.put("email", email);
                }
            }
        }

        return new Response(code, tokenCheckMessage[code], data);
    }

    /**
     * 验证 Token 是否有效并更改密码
     */
    @SuppressWarnings("unchecked")
    public Response resetPasswordByToken(String encodeToken, String password) {
        int code = 0;
        Response response = this.authTokenValid(encodeToken);
        if (response.getCode() == 0) {
            //可以重置密码
            HashMap<String, String> map = (HashMap<String, String>) response.getData();
            String email = map.get("email");
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            if (userMapper.changePasswordByToken(user) != 1) {
                code = 1;
            } else {
                // 数据库修改成功，可以删除 Redis
                String[] tokens = CommonUtil.verifyToken(encodeToken);
                redisUtil.del(resetPasswordPrefix + tokens[0]);
            }
        } else {
            // token不对，返回错误信息
            code = 2;
        }
        return new Response(code, resetPasswordMessage[code]);
    }

    /**
     * 验证旧密码并更改密码
     * ****此功能有待验证****
     */
    public Response resetPasswordByOldPassword(String oldPassword, String newPassword) {
        int code = 0;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("oldPassword", oldPassword);
        map.put("password", newPassword);
        if (userMapper.changePasswordByOldPassword(map) != 1) {
            //旧密码错误
            code = 3;
        }
        // 否则密码正确，修改成功
        return new Response(code, resetPasswordMessage[code]);
    }
}
