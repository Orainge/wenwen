package com.orainge.wenwen.mybatis.mapper;

import com.orainge.wenwen.mybatis.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    /* 获取用户登录时的信息 */
    /* principal 指的是用户名或邮箱，两者都可以 */
    public List<Map<String, Object>> getUserLoginInfo(String principal);
}