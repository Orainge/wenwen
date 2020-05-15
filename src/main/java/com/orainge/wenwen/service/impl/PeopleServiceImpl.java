package com.orainge.wenwen.service.impl;

import com.github.pagehelper.PageHelper;
import com.orainge.wenwen.mongo.dao.FollowDao;
import com.orainge.wenwen.mybatis.entity.User;
import com.orainge.wenwen.mybatis.mapper.QuestionMapper;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import com.orainge.wenwen.redis.util.RedisKey;
import com.orainge.wenwen.redis.util.RedisPrefix;
import com.orainge.wenwen.redis.util.RedisUtil;
import com.orainge.wenwen.service.PeopleService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PeopleServiceImpl implements PeopleService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private FollowDao followDao;
    @Autowired
    private RedisUtil redisUtil;
    private final Integer pageSize = 10;
    private final String PEOPLE_PREFIX = RedisPrefix.PEOPLE;
    private final String PEOPLE_COUNT_FOLLOWING_KEY = RedisKey.PEOPLE_COUNT_FOLLOWING;
    private final String PEOPLE_COUNT_FOLLOWER_KEY = RedisKey.PEOPLE_COUNT_FOLLOWER;

    @Override
    public Map<String, Object> toPeople(String peopleId) {
        User user = userMapper.getPeople(peopleId);
        if (user == null) {
            return null;
        }
        Integer followQuestionCount = followDao.getFollowQuestionCount(Integer.valueOf(peopleId));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("peopleId", Integer.valueOf(peopleId));
        result.put("nickname", user.getNickname() == null ? "" : user.getNickname());
        result.put("gender", user.getGender() == null ? "" : user.getGender());
        result.put("simpleDescription", user.getSimpleDescription() == null ? "" : user.getSimpleDescription());
        result.put("address", user.getAddress() == null ? "" : user.getAddress());
        result.put("industry", user.getIndustry() == null ? "" : user.getIndustry());
        result.put("career", user.getCareer() == null ? "" : user.getCareer());
        result.put("education", user.getEducation() == null ? "" : user.getEducation());
        result.put("fullDescription", user.getFullDescription() == null ? "" : user.getFullDescription());
        result.put("avatarUrl", user.getAvatarUrl() == null ? "" : user.getAvatarUrl());
        Integer countFollowing = redisUtil.hincrby(PEOPLE_PREFIX + peopleId, PEOPLE_COUNT_FOLLOWING_KEY, 0);
        Integer countFollower = redisUtil.hincrby(PEOPLE_PREFIX + peopleId, PEOPLE_COUNT_FOLLOWER_KEY, 0);
        result.put("countFollowing", countFollowing);
        result.put("countFollower", countFollower);
        result.put("countFollowQuestion", followQuestionCount);
        return result;
    }

    @Override
    public Response apiQuestion(Integer userId, Integer peopleId, Integer nextPage) {
        Response response = new Response();
        PageHelper.startPage(nextPage, pageSize);
        List<Map<String, Object>> list = questionMapper.getPeopleQuestion(userId, peopleId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        if (list.size() < pageSize) {
            result.put("end", 1);
        }
        response.setData(result);
        return response;
    }

    @Override
    public Response apiAnswer(Integer userId, Integer peopleId, Integer nextPage) {
        Response response = new Response();
        PageHelper.startPage(nextPage, pageSize);
        List<Map<String, Object>> list = questionMapper.getPeopleAnswer(userId, peopleId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        if (list.size() < pageSize) {
            result.put("end", 1);
        }
        response.setData(result);
        return response;
    }

    @Override
    public Response apiFollowQuestion(Integer peopleId, Integer nextPage) {
        Response response = new Response();
        List<Integer> questionList = followDao.getFollowQuestionIds(peopleId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> result = new HashMap<String, Object>();
        if (questionList != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", questionList);
            map.put("peopleId", peopleId);
            PageHelper.startPage(nextPage, pageSize);
            list = questionMapper.getPeopleFollowQuestion(map);
        }
        result.put("data", list);
        if (list.size() < pageSize) {
            result.put("end", 1);
        }
        response.setData(result);
        return response;
    }

    @Override
    public Response apiFollowUser(Integer peopleId, Integer nextPage) {
        Response response = new Response();
        List<Integer> userList = followDao.getFollowUserIds(peopleId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> result = new HashMap<String, Object>();
        if (userList != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", userList);
            PageHelper.startPage(nextPage, pageSize);
            list = questionMapper.getPeopleFollowUser(map);
        }
        result.put("data", list);
        if (list.size() < pageSize) {
            result.put("end", 1);
        }
        response.setData(result);
        return response;
    }
}
