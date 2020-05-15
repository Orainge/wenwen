package com.orainge.wenwen.service;

import com.orainge.wenwen.mybatis.entity.User;
import com.orainge.wenwen.util.Response;

import java.util.Map;

public interface SettingsService {
    public Map<String, Object> toProfile(String userId);

    public Response apiProfile(User user);

    public Response apiPassword(String userId, Map<String, String> passwordMap);

    public Response apiAvatar(Integer userId, String base64Data);
}
