package com.orainge.wenwen.service;

import com.orainge.wenwen.mybatis.entity.User;

public interface LoginService {
    public User userLogin(String username);
}