package com.orainge.wenwen.mapper;

import com.orainge.wenwen.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 通过账号查询用户信息
    User findUserByUsername(String username);
}
