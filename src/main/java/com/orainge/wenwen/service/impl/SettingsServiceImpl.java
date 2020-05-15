package com.orainge.wenwen.service.impl;

import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.mybatis.entity.User;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import com.orainge.wenwen.service.SettingsService;
import com.orainge.wenwen.util.ImageUtil;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SettingsServiceImpl implements SettingsService {
    @Autowired
    private ImageUtil imageUtil;
    @Autowired
    private UserMapper userMapper;

    @Override
    @SuppressWarnings("all")
    public Map<String, Object> toProfile(String userId) {
        User user = userMapper.getProfile(userId);
        if (user == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("nickname", user.getNickname() == null ? "" : user.getNickname());
        result.put("gender", user.getGender() == null ? "" : user.getGender());
        result.put("simpleDescription", user.getSimpleDescription() == null ? "" : user.getSimpleDescription());
        result.put("address", user.getAddress() == null ? "" : user.getAddress());
        result.put("industry", user.getIndustry() == null ? "" : user.getIndustry());
        result.put("career", user.getCareer() == null ? "" : user.getCareer());
        result.put("education", user.getEducation() == null ? "" : user.getEducation());
        result.put("fullDescription", user.getFullDescription() == null ? "" : user.getFullDescription());
        return result;
    }

    @Override
    public Response apiProfile(User user) {
        Response response = new Response();
        if (userMapper.setProfile(user) != 1) {
            response.setCode(2);
            response.setMessage("修改失败，请稍后再试");
        } else {
            response.setMessage("修改成功");
        }
        return response;
    }

    @Override
    public Response apiPassword(String userId, Map<String, String> passwordMap) {
        Response response = new Response();
        passwordMap.put("userId", userId);
        if (userMapper.changePasswordByOldPassword(passwordMap) != 1) {
            response.setCode(2);
            response.setMessage("保存失败，旧密码错误");
        } else {
            response.setMessage("设置成功");
        }
        return response;
    }

    @Override
    public Response apiAvatar(Integer userId, String base64Data) {
        Response response = new Response();
        String imageUrl = imageUtil.upload(base64Data, "avatar");
        if (imageUrl == null) {
            response.setCode(2);
            response.setMessage("保存失败，请稍后再试");
        } else {
            User user = new User();
            user.setUserId(userId);
            user.setAvatarUrl(imageUrl);
            if (userMapper.setAvatar(user) != 1) {
                response.setCode(2);
                response.setMessage("保存失败，请稍后再试");
            } else {
                response.setMessage("保存成功");
                Map<String, String> result = new HashMap<String, String>();
                result.put("avatarUrl", imageUrl);
                response.setData(result);
            }
        }
        return response;
    }
}
