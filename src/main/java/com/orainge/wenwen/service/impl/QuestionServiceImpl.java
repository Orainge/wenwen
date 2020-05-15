package com.orainge.wenwen.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.orainge.wenwen.exception.MySQLException;
import com.orainge.wenwen.exception.NotFoundException;
import com.orainge.wenwen.exception.type.MySQLError;
import com.orainge.wenwen.mongo.dao.MongoDBTopicDao;
import com.orainge.wenwen.mongo.dao.NotificationDao;
import com.orainge.wenwen.mongo.dao.SocialDao;
import com.orainge.wenwen.mongo.entity.NotificationData;
import com.orainge.wenwen.mongo.util.NotificationHelper;
import com.orainge.wenwen.mongo.util.NotificationType;
import com.orainge.wenwen.mybatis.entity.Answer;
import com.orainge.wenwen.mybatis.entity.Question;
import com.orainge.wenwen.mybatis.entity.Topic;
import com.orainge.wenwen.mybatis.mapper.AnswerMapper;
import com.orainge.wenwen.mybatis.mapper.QuestionMapper;
import com.orainge.wenwen.mybatis.mapper.TopicMapper;
import com.orainge.wenwen.redis.util.RedisKey;
import com.orainge.wenwen.redis.util.RedisPrefix;
import com.orainge.wenwen.redis.util.RedisUtil;
import com.orainge.wenwen.service.QuestionService;
import com.orainge.wenwen.util.Response;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final String QUESTION_PREFIX = RedisPrefix.QUESTION;
    private final String COUNT_FOLLOW_KEY = RedisKey.QUESTION_COUNT_FOLLOW;
    private final String COUNT_BROWSE_KEY = RedisKey.QUESTION_COUNT_BROWSE;
    private final String ANSWER_PREFIX = RedisPrefix.ANSWER;
    private final String ANSWER_COUNT_LIKE_KEY = RedisKey.ANSWER_COUNT_LIKE;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private SocialDao socialDao;

    @Autowired
    private MongoDBTopicDao mongoDBTopicDao;


    @Override
    public Map<String, String> toQuestion(Integer questionId, String userId) {
        Map<String, Object> result = questionMapper.queryQuestion(questionId);
        if (result == null) {
            throw new NotFoundException("问题");
        }
        Integer countBrowse = redisUtil.hincrby(QUESTION_PREFIX + questionId, COUNT_BROWSE_KEY, 1);
        Integer countFollow = redisUtil.hincrby(QUESTION_PREFIX + questionId, COUNT_FOLLOW_KEY, 0);
        result.put("count_follow", countFollow);
        result.put("count_browse", countBrowse);
        result.put("question_id", questionId);
        Map<String, String> map = new HashMap<String, String>();
        for (String key : result.keySet()) {
            map.put(key, JSON.toJSONString(result.get(key)));
        }
        if (!(map.get("user_id").equals(userId)) && map.get("anonymous").equals("1")) {
            map.remove("user_id");
            map.remove("username");
            map.remove("avatar_url");
            map.remove("simple_description");
        }
        return map;
    }

    @Override
    public Response deleteQuestion(Integer questionId, String userId) {
        Response response = new Response();
        // 删除数据库
        Question question = new Question();
        question.setQuestionId(questionId);
        question.setUserId(Integer.valueOf(userId));
        question.setIsDelete(1);
        if (1 != questionMapper.deleteQuestion(question)) {
            throw new MySQLException(MySQLError.INSERT_ERROR, "删除问题失败");
        }

        //删除通知
        NotificationData notificationData = new NotificationData();
        notificationData.setQ_id(questionId);
        notificationDao.deleteMessage(Integer.parseInt(userId), NotificationType.ASK, notificationData);
        response.setMessage("删除成功");
        return response;
    }

    @Override
    public Response apiGetAnswerById(Integer userId, Integer questionId, Integer answerId) {
        Response response = new Response();
        Answer answer = new Answer();
        answer.setQuestionId(questionId);
        answer.setAnswerId(answerId);
        Map<String, Object> result = answerMapper.getAnswerById(answer);
        Map<String, Object> data = new HashMap<String, Object>();
        if (result == null || result.size() == 0) {
            throw new NotFoundException();
        } else {
            Integer answerUserId = (Integer) result.get("userId");
            if (userId != answerUserId && (Integer) result.get("anonymous") == 1) {
                result.remove("userId");
                result.remove("nickname");
                result.remove("avatarUrl");
                result.remove("simpleDescription");
            }
            result.remove("anonymous");
            result.put("isLike", socialDao.isLikeAnswer(userId, (Integer) result.get("answerId")));
            result.put("countLike", redisUtil.hincrby(ANSWER_PREFIX + result.get("answerId"), ANSWER_COUNT_LIKE_KEY, 0));
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            list.add(result);
            data.put("data", list);
            data.put("end", 1);
        }
        response.setData(data);
        return response;
    }

    // 打开编辑问题页面
    @Override
    public Map<String, Object> toEditQuestion(Integer questionId) {
        Question question = questionMapper.getQuestionToEdit(questionId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", JSON.toJSONString(question.getTitle()));
        map.put("content", JSON.toJSONString(question.getContent()));
        map.put("anonymous", JSON.toJSONString(question.getAnonymous()));

        List<Topic> list = JSONObject.parseArray(question.getTopicList(), Topic.class);
        List<String> nlist = new ArrayList<String>();
        for (Topic t : list) {
            nlist.add(t.getTopicName());
        }
        map.put("topicList", JSON.toJSONString(JSON.toJSONString(nlist)));
        return map;
    }

    // 保存编辑的问题
    @Override
    public Response apiEdit(Integer questionId, String title, String content, Integer anonymous, List<String> topicIdList) {
        Response response = new Response();
        Question question = new Question();
        question.setQuestionId(questionId);
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
//        if (resultTopicList.size() == 0) {
//            throw new MySQLException(MySQLError.INSERT_ERROR, "创建新话题");
//        }

        for (int i = 0; i < topicIdList.size(); i++) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("topic_id", resultTopicList.get(i).getTopicId());
            m.put("topic_name", resultTopicList.get(i).getTopicName());
            topicList.add(m);
            topicIds.add(resultTopicList.get(i).getTopicId());
        }
        question.setTopicList(JSON.toJSONString(topicList));

        if (questionMapper.saveEditQuestion(question) != 1) {
            throw new MySQLException(MySQLError.INSERT_ERROR, "修改问题");
        }

        Map<String, Object> mongoDBQuestionObject = new HashMap<String, Object>();
        mongoDBQuestionObject.put("question_id", question.getQuestionId());
        mongoDBQuestionObject.put("title", title);
        mongoDBTopicDao.saveQuestionToTopic(topicIds, mongoDBQuestionObject);

        response.setData(question.getQuestionId());
        return response;
    }

}
