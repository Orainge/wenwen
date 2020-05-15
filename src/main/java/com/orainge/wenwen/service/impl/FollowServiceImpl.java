package com.orainge.wenwen.service.impl;

import com.orainge.wenwen.mongo.dao.FollowDao;
import com.orainge.wenwen.mongo.dao.NotificationDao;
import com.orainge.wenwen.mongo.entity.NotificationData;
import com.orainge.wenwen.mongo.util.NotificationType;
import com.orainge.wenwen.mybatis.mapper.QuestionMapper;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import com.orainge.wenwen.redis.util.RedisKey;
import com.orainge.wenwen.redis.util.RedisPrefix;
import com.orainge.wenwen.redis.util.RedisUtil;
import com.orainge.wenwen.service.FollowService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private FollowDao followDao;
    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;
    private final String QUESTION_PREFIX = RedisPrefix.QUESTION;
    private final String QUESTION_COUNT_FOLLOW_KEY = RedisKey.QUESTION_COUNT_FOLLOW;
    private final String PEOPLE_PREFIX = RedisPrefix.PEOPLE;
    private final String PEOPLE_COUNT_FOLLOWING_KEY = RedisKey.PEOPLE_COUNT_FOLLOWING;
    private final String PEOPLE_COUNT_FOLLOWER_KEY = RedisKey.PEOPLE_COUNT_FOLLOWER;

    @Override
    public Response apiCheckFollowPeople(Integer userId, Integer peopleId) {
        Response response = new Response();
        Map<String, Integer> data = new HashMap<String, Integer>();
        if (followDao.isFollowPeople(userId, peopleId)) {
            data.put("isFollow", 1);
        } else {
            data.put("isFollow", 0);
        }
        response.setData(data);
        return response;
    }

    @Override
    public Response apiFollowPeople(Integer userId, String userNickname, Integer peopleId, String peopleNickname) {
        Response response = new Response();
        followDao.followPeople(userId, peopleId);
        notificationDao.saveMessage(userId, userNickname, peopleId, peopleNickname, NotificationType.FOLLOW_USER);
        // redis 增加两个值
        // 一个是关注的人的数字
        // 一个是被关注人的数字
        Integer countFollowing = redisUtil.hincrby(PEOPLE_PREFIX + userId, PEOPLE_COUNT_FOLLOWING_KEY, 1);
        Integer countFollower = redisUtil.hincrby(PEOPLE_PREFIX + peopleId, PEOPLE_COUNT_FOLLOWER_KEY, 1);
        response.setData(countFollower);
        response.setMessage("关注成功");
        return response;
    }

    @Override
    public Response apiUnFollowPeople(Integer userId, Integer peopleId) {
        Response response = new Response();
        followDao.unFollowPeople(userId, peopleId);
        notificationDao.deleteMessage(userId, peopleId, NotificationType.FOLLOW_USER);
        // redis 删除两个值
        // 一个是关注的人的数字
        // 一个是被关注人的数字
        Integer countFollowing = redisUtil.hincrby(PEOPLE_PREFIX + userId, PEOPLE_COUNT_FOLLOWING_KEY, -1);
        Integer countFollower = redisUtil.hincrby(PEOPLE_PREFIX + peopleId, PEOPLE_COUNT_FOLLOWER_KEY, -1);
        response.setData(countFollower);
        response.setMessage("取消关注成功");
        return response;
    }

    @Override
    public Response apiCheckFollowQuestion(Integer userId, Integer questionId) {
        Response response = new Response();
        Map<String, Integer> data = new HashMap<String, Integer>();
        if (followDao.isFollowQuestion(userId, questionId)) {
            data.put("isFollow", 1);
        } else {
            data.put("isFollow", 0);
        }
        response.setData(data);
        return response;
    }

    @Override
    public Response apiFollowQuestion(Integer userId, Integer questionId) {
        Response response = new Response();
        followDao.followQuestion(userId, questionId);
        Map<String, Object> info = questionMapper.getFollowPeopleInfo(questionId);
        NotificationData notificationData = new NotificationData();
        notificationData.setQ_id(questionId);
        notificationData.setTitle((String) info.get("title"));
        notificationDao.saveMessage(userId, userMapper.getNickname(userId), (Integer) info.get("questionUserId"), (String) info.get("nickname"), NotificationType.FOLLOW_QUESTION, notificationData);
        // 增加关注人数在redis
        Integer countFollow = redisUtil.hincrby(QUESTION_PREFIX + questionId, QUESTION_COUNT_FOLLOW_KEY, 1);
        response.setMessage("关注成功");
        response.setData(countFollow);
        return response;
    }

    @Override
    public Response apiUnFollowQuestion(Integer userId, Integer questionId) {
        Response response = new Response();
        followDao.unFollowQuestion(userId, questionId);
        NotificationData notificationData = new NotificationData();
        notificationData.setQ_id(questionId);
        notificationDao.deleteMessage(userId, NotificationType.FOLLOW_QUESTION, notificationData);
        Integer countFollow = redisUtil.hincrby(QUESTION_PREFIX + questionId, QUESTION_COUNT_FOLLOW_KEY, -1);
        response.setMessage("取消关注成功");
        response.setData(countFollow);
        return response;
    }
}
