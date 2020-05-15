package com.orainge.wenwen.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.orainge.wenwen.exception.MySQLException;
import com.orainge.wenwen.exception.type.MySQLError;
import com.orainge.wenwen.mongo.dao.NotificationDao;
import com.orainge.wenwen.mongo.dao.SocialDao;
import com.orainge.wenwen.mongo.entity.NotificationData;
import com.orainge.wenwen.mongo.util.NotificationType;
import com.orainge.wenwen.mybatis.entity.Answer;
import com.orainge.wenwen.mybatis.entity.User;
import com.orainge.wenwen.mybatis.mapper.AnswerMapper;
import com.orainge.wenwen.mybatis.mapper.QuestionMapper;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import com.orainge.wenwen.redis.util.RedisKey;
import com.orainge.wenwen.redis.util.RedisPrefix;
import com.orainge.wenwen.redis.util.RedisUtil;
import com.orainge.wenwen.service.AnswerService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.IntegerSyntax;
import java.util.*;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private RedisUtil redisUtil;
    private final String DRAFT_ANSWER_PREFIX = RedisPrefix.DRAFT_ANSWER;
    private final String ANSWER_PREFIX = RedisPrefix.ANSWER;
    private final String ANSWER_COUNT_LIKE_KEY = RedisKey.ANSWER_COUNT_LIKE;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private SocialDao socialDao;

    private Integer pageSize = 10;

    @Override
    public Map<String, String> toAnswer(Integer userId, Integer questionId) {
        Map<String, String> redisDraftObj = redisUtil.hgetAll(getKey(userId, questionId));
        if (!(redisDraftObj == null || redisDraftObj.isEmpty())) {
            // 说明这个账户有草稿
            Map<String, String> draft = new HashMap<String, String>();
            draft.put("content", redisDraftObj.get("content"));
            draft.put("anonymous", redisDraftObj.get("anonymous"));
            draft.put("isShort", redisDraftObj.get("isShort"));
            return draft;
        }
        return null;
    }

    @Override
    public Response apiAnswer(Integer userId, Integer questionId, String content, Integer anonymous, Integer isShort) {
        Response response = new Response();
        Answer answer = new Answer();
        answer.setUserId(userId);
        answer.setQuestionId(questionId);
        answer.setContent(content);
        answer.setAnonymous(anonymous);
        answer.setIsShort(isShort);
        answer.setCreateTime(new Date());

        if (answerMapper.insertAnswer(answer) != 1) {
            throw new MySQLException(MySQLError.INSERT_ERROR, "创建新回答");
        }

        //增加回答条数
        if (questionMapper.addQuestionAnswerCount(questionId) != 1) {
            throw new MySQLException(MySQLError.INSERT_ERROR, "更新回答条数");
        }

        redisUtil.del(getKey(userId, questionId));
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("questionId", questionId);
        data.put("answerId", answer.getAnswerId());
        response.setData(data);

        // 发布通知
        Map<String, Object> datas = questionMapper.getFollowPeopleInfo(questionId);
        NotificationData notificationData = new NotificationData();
        notificationData.setQ_id(questionId);
        notificationData.setTitle((String) datas.get("title"));
        notificationData.setA_id(answer.getAnswerId());
        if (anonymous == 0) {
            notificationDao.saveMessage(userId, userMapper.getNickname(userId), (Integer) datas.get("questionUserId"), (String) datas.get("nickname"), NotificationType.ANSWER, notificationData);
        } else if (anonymous == 1) {
            notificationDao.saveMessage(null, null, (Integer) datas.get("questionUserId"), (String) datas.get("nickname"), NotificationType.ANSWER, notificationData);
        }
        return response;
    }

    @Override
    public Response apiSaveDraft(Integer userId, Integer questionId, String content, Integer anonymous, Integer isShort) {
        Response response = new Response();
        Map<String, String> hPuts = new HashMap<String, String>();
        hPuts.put("content", content);
        hPuts.put("anonymous", String.valueOf(anonymous));
        hPuts.put("isShort", String.valueOf(isShort));
        redisUtil.hset(getKey(userId, questionId), hPuts);
        response.setMessage("保存成功");
        return response;
    }

    @Override
    public Response apiDelete(Integer userId, Integer answerId) {
        Response response = new Response();
        // 删除数据库
        Answer answer = new Answer();
        answer.setAnswerId(answerId);
        answer.setUserId(userId);
        answer.setIsDelete(1);
        if (1 != answerMapper.deleteAnswer(answer)) {
            throw new MySQLException(MySQLError.DELETE_ERROR, "删除回答失败");
        }

        // 回答数-1
        questionMapper.deleteQuestionAnswerCount(answerId);

        //删除通知
        NotificationData notificationData = new NotificationData();
        notificationData.setA_id(answerId);
        notificationDao.deleteMessage(userId, NotificationType.ANSWER, notificationData);
        response.setMessage("删除成功");
        return response;
    }

    private String getKey(Integer userId, Integer questionId) {
        return DRAFT_ANSWER_PREFIX + userId + ":" + questionId;
    }

    @Override
    public Response apiGetAnswer(Integer questionId, String userId, Integer nextPage) {
        Response response = new Response();
        Answer answer = new Answer();
        answer.setQuestionId(questionId);
        answer.setUserId(Integer.parseInt(userId));
        PageHelper.startPage(nextPage, pageSize);
        List<Map<String, Object>> result = answerMapper.getAnswerByQuestionId(answer);
        Map<String, Object> data = new HashMap<String, Object>();
        if (result == null || result.size() == 0) {
            data.put("end", 1);
        } else {
            List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < result.size(); i++) {
                Map<String, Object> temp = result.get(i);
                Integer answerUserId = (Integer) temp.get("userId");
                if (Integer.parseInt(userId) != answerUserId && (Integer) temp.get("anonymous") == 1) {
                    temp.remove("userId");
                    temp.remove("nickname");
                    temp.remove("avatarUrl");
                    temp.remove("simpleDescription");
                }
                temp.remove("anonymous");
                temp.put("isLike", socialDao.isLikeAnswer(Integer.parseInt(userId), (Integer) temp.get("answerId")));
                temp.put("countLike", redisUtil.hincrby(ANSWER_PREFIX + temp.get("answerId"), ANSWER_COUNT_LIKE_KEY, 0));
                newList.add(temp);
            }
            data.put("data", newList);
            if (result.size() < pageSize) {
                data.put("end", 1);
            }
        }
        response.setData(data);
        return response;
    }

    @Override
    public Response apiLikeAnswer(Integer userId, Integer answerId) {
        Response response = new Response();
        // 添加 Redis
        Integer countLike = redisUtil.hincrby(ANSWER_PREFIX + answerId, ANSWER_COUNT_LIKE_KEY, 1);
        // 添加到 MongoDB 点赞列表
        socialDao.likeAnswer(userId, answerId);

        // 添加 MongoDB 通知
        Map<String, Object> info = answerMapper.getAnswerInfoByAnswerId(answerId);
        NotificationData notificationData = new NotificationData();
        notificationData.setQ_id((Integer) info.get("questionId"));
        notificationData.setTitle((String) info.get("title"));
        notificationData.setA_id(answerId);
        notificationDao.saveMessage(userId, userMapper.getNickname(userId), (Integer) info.get("userId"), (String) info.get("nickname"), NotificationType.LIKE_ANSWER, notificationData);
        response.setData(countLike);
        response.setMessage("点赞成功");
        return response;
    }

    @Override
    public Response apiUnLikeAnswer(Integer userId, Integer answerId) {
        Response response = new Response();
        // 删除 Redis
        Integer countLike = redisUtil.hincrby(ANSWER_PREFIX + answerId, ANSWER_COUNT_LIKE_KEY, -1);
        // 删除 MongoDB 点赞列表
        socialDao.unLikeAnswer(userId, answerId);

        // 删除 MongoDB 通知
        NotificationData notificationData = new NotificationData();
        notificationData.setA_id(answerId);
        notificationDao.deleteMessage(userId, NotificationType.LIKE_ANSWER, notificationData);
        response.setData(countLike);
        response.setMessage("取消点赞成功");
        return response;
    }
}