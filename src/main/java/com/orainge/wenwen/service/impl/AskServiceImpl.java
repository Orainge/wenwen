package com.orainge.wenwen.service.impl;

import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.exception.MySQLException;
import com.orainge.wenwen.exception.type.MySQLError;
import com.orainge.wenwen.mongo.dao.MongoDBTopicDao;
import com.orainge.wenwen.mongo.dao.NotificationDao;
import com.orainge.wenwen.mongo.entity.NotificationData;
import com.orainge.wenwen.mongo.util.NotificationType;
import com.orainge.wenwen.mybatis.entity.Question;
import com.orainge.wenwen.mybatis.entity.Topic;
import com.orainge.wenwen.mybatis.mapper.QuestionMapper;
import com.orainge.wenwen.mybatis.mapper.TopicMapper;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import com.orainge.wenwen.redis.util.RedisPrefix;
import com.orainge.wenwen.redis.util.RedisUtil;
import com.orainge.wenwen.service.AskService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AskServiceImpl implements AskService {
    @Autowired
    private RedisUtil redisUtil;
    private final String REDIS_DRAFT_QUESTION_PREFIX = RedisPrefix.DRAFT_QUESTION;
    private final String REDIS_QUESTION_PREFIX = RedisPrefix.QUESTION;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private MongoDBTopicDao mongoDBTopicDao;
    @Autowired
    private NotificationDao notificationDao;

    @Override
    public Map<String, String> toAsk(String userId) {
        Map<String, String> redisDraftObj = redisUtil.hgetAll(REDIS_DRAFT_QUESTION_PREFIX + userId);
        if (!(redisDraftObj == null || redisDraftObj.isEmpty())) {
            // 说明这个账户有草稿
            Map<String, String> draft = new HashMap<String, String>();
            draft.put("title", redisDraftObj.get("title"));
            draft.put("content", redisDraftObj.get("content"));
            draft.put("anonymous", redisDraftObj.get("anonymous"));
            draft.put("topicList", JSON.toJSONString(redisDraftObj.get("topicList")));
            return draft;
        }
        return null;
    }

    @Override
    public Response apiAsk(Integer userId, String title, String content, Integer anonymous, List<String> topicIdList) {
        Response response = new Response();
        Question question = new Question();
        question.setUserId(userId);
        question.setTitle(title);
        question.setContent(content);
        question.setAnonymous(anonymous);
        question.setCreateTime(new Date());

        List<Topic> topics = new ArrayList<Topic>();
        List<Integer> topicIds = new ArrayList<Integer>();
        List<Object> topicList = new ArrayList<Object>();

        for (String s : topicIdList) {
            Topic topic = new Topic();
            topic.setTopicName(s);
            topics.add(topic);
        }

        List<Topic> resultTopicList = topicMapper.insertTopic(topics);
        if (resultTopicList.size() == 0) {
            throw new MySQLException(MySQLError.INSERT_ERROR, "创建新话题");
        }

        for (int i = 0; i < topicIdList.size(); i++) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("topic_id", resultTopicList.get(i).getTopicId());
            m.put("topic_name", resultTopicList.get(i).getTopicName());
            topicList.add(m);
            topicIds.add(resultTopicList.get(i).getTopicId());
        }
        question.setTopicList(JSON.toJSONString(topicList));

        if (questionMapper.insertQuestion(question) != 1) {
            throw new MySQLException(MySQLError.INSERT_ERROR, "创建新问题");
        }

        Map<String, Object> mongoDBQuestionObject = new HashMap<String, Object>();
        mongoDBQuestionObject.put("question_id", question.getQuestionId());
        mongoDBQuestionObject.put("title", title);
        mongoDBTopicDao.saveQuestionToTopic(topicIds, mongoDBQuestionObject);

        redisUtil.del(REDIS_DRAFT_QUESTION_PREFIX + userId);
        redisUtil.hincrby(REDIS_QUESTION_PREFIX + userId, "count_browse", 0);
        redisUtil.hincrby(REDIS_QUESTION_PREFIX + userId, "count_follow", 0);
        response.setData(question.getQuestionId());

        // 创建通知
        NotificationData notificationData = new NotificationData();
        notificationData.setQ_id(question.getQuestionId());
        notificationData.setTitle(title);
        if (anonymous == 0) {
            notificationDao.saveMessage(userId, userMapper.getNickname(userId), NotificationType.ASK, notificationData);
        }else if(anonymous == 1){
            notificationDao.saveMessage(null, null, NotificationType.ASK, notificationData);
        }
        return response;
    }

    @Override
    public Response apiSaveDraft(Integer userId, String title, String content, Integer anonymous, String topicList) {
        Response response = new Response();
        String redisKey = REDIS_DRAFT_QUESTION_PREFIX + userId;
        Map<String, String> hPuts = new HashMap<String, String>();
        hPuts.put("title", title);
        hPuts.put("content", content);
        hPuts.put("anonymous", String.valueOf(anonymous));
        hPuts.put("topicList", topicList);
        redisUtil.hset(redisKey, hPuts);
        response.setMessage("保存成功");
        return response;
    }
}