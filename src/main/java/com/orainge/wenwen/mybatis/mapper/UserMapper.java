package com.orainge.wenwen.mybatis.mapper;

import com.orainge.wenwen.mybatis.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface UserMapper {
    public User getUserAuthInfoByPricipal(String principal);

    public User getUserInfoByPricipal(String principal);

    public List<User> checkUserIsExists(User user);

    public int registerUser(User user);

    public int updateLoginTime(User user);

    public int activateUser(String email);

    public User getUserStatus(String email);

    public int changePasswordByToken(User user);

    public int changePasswordByOldPassword(HashMap<String, String> map);
}