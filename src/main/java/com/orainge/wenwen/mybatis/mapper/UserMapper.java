package com.orainge.wenwen.mybatis.mapper;

import com.orainge.wenwen.mybatis.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    /**
     * 获取用户登录时的信息
     *
     * @param principal 用户名或邮箱，两者都可以
     * @return 获取到的用户信息
     */
    public User getUserLoginInfoByPricipal(String principal);

    /**
     * 获取用户信息
     *
     * @param principal 用户名或邮箱，两者都可以
     * @return 用户信息
     */
    public User getUserInfoByPricipal(String principal);
}