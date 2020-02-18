package com.orainge.wenwen.service.impl;

import com.orainge.wenwen.mybatis.mapper.UserMapper;
import com.orainge.wenwen.mybatis.entity.User;
import com.orainge.wenwen.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserMapper userMapper;

    @Override
    // 通过账号查询用户信息
    public User userLogin(String username) {
        return null;
    }
}