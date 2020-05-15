package com.orainge.wenwen.shiro.util;

import com.alibaba.druid.util.StringUtils;
import com.orainge.wenwen.exception.MySQLException;
import com.orainge.wenwen.exception.EmailException;
import com.orainge.wenwen.exception.type.MySQLError;
import com.orainge.wenwen.exception.type.EmailError;
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
public class RegisterUtil {
    @Autowired
    UserMapper userMapper;
    @Autowired
    EmailUtil emailUtil;
    @Autowired
    RedisUtil redisUtil;

    // 错误提示 状态码对应数组的下标
    private final String[] registerMessage = {"注册成功", "连接错误，请稍后再试", "邮箱已存在，换一个试试呗", "用户名已存在，换一个试试呗"};
    private final String[] sendActivateMessage = {"激活链接发送成功，请注意查收", "连接错误，请稍后再试", "用户不存在", "该用户已经激活"};
    private final String[] activateMessage = {"验证成功", "连接错误，请稍后再试", "激活链接无效"};
    private final String activateEmailPrefix = RedisPrefix.EMAIL_ACTIVE;

    /**
     * 注册账户
     */
    public Response register(String email, String username, String password) {
        int code = 0;
        User regUser = new User();
        regUser.setEmail(email);
        regUser.setUsername(username);
        List<User> dbUsers = userMapper.checkUserIsExists(regUser);
        if (dbUsers.isEmpty()) {
            // 不存在用户，执行注册操作
            regUser.setPassword(password);
            regUser.setCreateTime(new Date());
            regUser.setAvatarUrl("/static/img/icon/avatar.png");
            if (userMapper.registerUser(regUser) != 1) {
                throw new MySQLException(MySQLError.INSERT_ERROR, "用户注册");
            }
            if (sendActivateEmail(email).getCode() != 0) {
                throw new EmailException(EmailError.SEND);
            }
        } else {
            // 存在用户
            for (User dbUser : dbUsers) {
                if (StringUtils.equals(email, dbUser.getEmail())) {
                    code = 2;
                    break;
                } else if (StringUtils.equals(username, dbUser.getUsername())) {
                    code = 3;
                    break;
                }
            }
        }
        return new Response(code, registerMessage[code]);
    }

    /**
     * 验证 Token 是否有效并激活账户
     */
    public Response activateByToken(String encodeToken) {
        int code = 0;
        String email = null;
        String[] tokens = CommonUtil.verifyToken(encodeToken);
        if (tokens == null) {
            //token 无效
            code = 2;
        } else {
            String encodeEmail = tokens[0];
            String originalToken = tokens[1];
            String redisKey = activateEmailPrefix + encodeEmail;

            Map<String, String> redisTokenObj = redisUtil.hgetAll(redisKey);
            if (redisTokenObj == null || redisTokenObj.isEmpty()) {
                // 说明这个账户没申请过激活链接
                code = 2;
            } else {
                // 说明这个账户申请过激活链接
                email = redisTokenObj.get("email");
                String redisToken = redisTokenObj.get("token");

                if (StringUtils.equals(redisToken, originalToken)) {
                    // 相等，可以激活
                    int status = userMapper.activateUser(email);
                    if (status != 1) {
                        code = 2;
                    }
                    try {
                        redisUtil.del(activateEmailPrefix + encodeEmail);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 数据库已经修改，Redis 删不删掉暂时忽略
                    }
                } else {
                    // token错误，激活链接无效
                    code = 2;
                }
            }
        }
        return new Response(code, activateMessage[code], email);
    }

    /**
     * 发送激活邮件
     *
     * @return
     */
    public Response sendActivateEmail(String email) {
        int code = 0;
        User user = userMapper.getUserStatus(email);
        if (user == null) {
            // 说明不存在这个用户
            code = 2;
        } else if (user.getUserStatus() == 0) {
            // 用户已经激活了
            code = 3;
        } else {
            // 用户需要激活
            String[] tokens = CommonUtil.getToken(email);
            String redisKey = activateEmailPrefix + tokens[0];
            String originalToken = tokens[1];
            String encodeToken = tokens[2];

            Map<String, String> hPuts = new HashMap<String, String>();
            hPuts.put("token", originalToken);
            hPuts.put("email", email);
            redisUtil.hset(redisKey, hPuts, 10 * 60);
            emailUtil.sendActivateEmail(email, encodeToken);
        }
        return new Response(code, sendActivateMessage[code]);
    }
}
