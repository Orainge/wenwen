package com.orainge.wenwen.shiro.util;

import com.alibaba.druid.util.StringUtils;
import com.orainge.wenwen.mybatis.entity.User;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import com.orainge.wenwen.redis.RedisUtil;
import com.orainge.wenwen.util.EmailUtil;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RegisterUtil {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    EmailUtil emailUtil;

    // 错误提示 状态码对应数组的下标
    private final String[] registerMessage = {"注册成功", "连接错误，请稍后再试", "邮箱已存在，换一个试试呗", "用户名已存在，换一个试试呗"};
    private final String[] activateMessage = {"验证成功", "连接错误，请稍后再试", "激活链接无效"};
    private final String[] sendActivateMessage = {"激活链接发送成功，请注意查收", "连接错误，请稍后再试", "用户不存在", "该用户已经激活"};
    private final String activateEmailPrefix = "activate:";

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
            try {
                regUser.setPassword(password);
                regUser.setCreateTime(new Date());
                if (userMapper.registerUser(regUser) != 1) {
                    throw new Exception("往数据库添加数据时发生错误");
                }
                if (sendActivateEmail(email).getCode() != 0) {
                    throw new Exception("发送注册邮件失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                code = 1;
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
        String email = "";
        try {
            String[] tokens = CommonUtil.verifyToken(encodeToken);
            if (tokens == null) {
                //token 无效
                code = 2;
            } else {
                String encodeEmail = tokens[0];
                String originalToken = tokens[1];
                String redisKey = activateEmailPrefix + encodeEmail;

                Collection<Object> hGets = new HashSet<Object>();
                hGets.add("email");
                hGets.add("token");
                List<Object> redisTokenObj = redisUtil.hMultiGet(redisKey, hGets);
                if (redisTokenObj.get(0) == null) {
                    // 说明这个账户没申请过激活链接
                    code = 2;
                } else {
                    // 说明这个账户申请过激活链接
                    email = redisTokenObj.get(0).toString();
                    String redisToken = redisTokenObj.get(1).toString();

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
        } catch (Exception e) {
            e.printStackTrace();
            code = 1;
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
        try {
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

                Map<String, Object> hPuts = new HashMap<String, Object>();
                hPuts.put("token", originalToken);
                hPuts.put("email", email);
                redisUtil.hPutAll(redisKey, hPuts);
                redisUtil.expire(redisKey, 10, TimeUnit.MINUTES);
                if (emailUtil.sendRegisterEmail(email, encodeToken).getCode() != 0) {
                    throw new Exception("发送邮件时发生错误");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            code = 1;
        }
        return new Response(code, sendActivateMessage[code]);
    }
}
