package com.orainge.wenwen.mybatis.mapper;

import com.orainge.wenwen.mybatis.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    public User getUserAuthInfoByPrincipal(String principal);

    public User getUserInfoByPrincipal(String principal);

    public List<User> checkUserIsExists(User user);

    public int registerUser(User user);

    public int updateLoginTime(User user);

    public int activateUser(String email);

    public User getUserStatus(String email);

    public int changePasswordByToken(User user);

    public int changePasswordByOldPassword(Map<String, String> map);

    public int setAvatar(User user);

    public User getProfile(String userId);

    public int setProfile(User user);

    public User getPeople(String peopleId);

    public List<User> searchUser(@Param("keyword") String keyword);

    public int updateRedisInfo(List<Map<String, String>> list);

    public String getNickname(@Param("userId") Integer userId);
}