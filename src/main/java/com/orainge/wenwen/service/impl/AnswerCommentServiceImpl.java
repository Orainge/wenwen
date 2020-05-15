package com.orainge.wenwen.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.orainge.wenwen.exception.MySQLException;
import com.orainge.wenwen.exception.NotFoundException;
import com.orainge.wenwen.exception.type.MySQLError;
import com.orainge.wenwen.mongo.dao.NotificationDao;
import com.orainge.wenwen.mongo.dao.SocialDao;
import com.orainge.wenwen.mongo.entity.NotificationData;
import com.orainge.wenwen.mongo.util.NotificationType;
import com.orainge.wenwen.mybatis.entity.AnswerComment;
import com.orainge.wenwen.mybatis.mapper.AnswerCommentMapper;
import com.orainge.wenwen.mybatis.mapper.AnswerMapper;
import com.orainge.wenwen.mybatis.mapper.QuestionMapper;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import com.orainge.wenwen.redis.util.RedisUtil;
import com.orainge.wenwen.service.AnswerCommentService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnswerCommentServiceImpl implements AnswerCommentService {
    @Autowired
    private AnswerCommentMapper answerCommentMapper;
    @Autowired
    private NotificationDao notificationDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SocialDao socialDao;
    private final String ANSWER_COMMENT_PREFIX = "answer_comment:";
    private final String ANSWER_COMMENT_COUNT_LIKE_KEY = "count_like";
    private Integer pageSize = 10;


    @Override
    public Response getAnswerComment(Integer userId, Integer answerId, Integer nextPage) {
        Response response = new Response();
        PageHelper.startPage(nextPage, pageSize);
        List<Map<String, Object>> list = answerCommentMapper.getAnswerComment(answerId);
        if (list == null)
            list = new ArrayList<>();
        List<Map<String, Object>> nlist = new ArrayList<Map<String, Object>>();

        Iterator<Map<String, Object>> iterator = list.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> m = iterator.next();
            String answerCommentId = String.valueOf(m.get("commentId"));
            if ((Integer) m.get("isDelete") == 1) {
                m.remove("content");
            } else {
                m.put("countLike", redisUtil.hincrby(ANSWER_COMMENT_PREFIX + answerCommentId, ANSWER_COMMENT_COUNT_LIKE_KEY, 0));
                m.put("isLike", socialDao.isLikeAnswerComment(userId, Integer.parseInt(answerCommentId)));
                m.remove("isDelete");
            }
            Integer atUserId = (Integer) m.get("atUserId");
            if (atUserId != null) {
                m.put("atNickname", userMapper.getNickname(atUserId));
            }
            nlist.add(m);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", nlist);
        if (nlist.size() < pageSize) {
            result.put("end", 1);
        }
        response.setData(result);
        return response;
    }

    @Override
    public Response apiAnswerComment(Integer userId, Integer answerId, String content, Integer questionId, Integer atUserId) {
        Response response = new Response();
        AnswerComment answerComment = new AnswerComment();
        answerComment.setContent(content);
        answerComment.setCreateTime(new Date());
        answerComment.setAnswerId(answerId);
        answerComment.setUserId(userId);
        if (atUserId != null) {
            answerComment.setAtUserId(atUserId);
        }
        answerCommentMapper.saveAnswerComment(answerComment);
        Integer answerCommentId = answerComment.getCommentId();

        // 添加评论条数
        answerMapper.addAnswerCommentCount(answerId);

        // 根据情况来写入 MongoDB 信息
        // 1. 无论什么情况，都要写入 回答有评论 的通知
        NotificationData notificationData = new NotificationData();
        notificationData.setQ_id(questionId);
        notificationData.setTitle(questionMapper.getQuestionTitle(questionId).getTitle());
        notificationData.setAc_id(answerCommentId);
        Integer answerUserId = answerMapper.getAnswerUserId(answerId).getUserId();
        notificationDao.saveMessage(userId, userMapper.getNickname(userId), answerUserId, userMapper.getNickname(answerUserId), NotificationType.COMMENT_ANSWER, notificationData);
        if (atUserId != null) {
            // 2.如果是 回复回答评论 ，还需要通知被回复的人
            notificationData = new NotificationData();
            notificationData.setQ_id(questionId);
            notificationData.setTitle(questionMapper.getQuestionTitle(questionId).getTitle());
            notificationData.setAc_id(answerCommentId);
            notificationDao.saveMessage(userId, userMapper.getNickname(userId), atUserId, userMapper.getNickname(atUserId), NotificationType.REPLY_COMMENT_ANSWER, notificationData);
        }

        response.setMessage("提交成功");
        return response;
    }

    @Override
    public Response apiDeleteAnswerComment(Integer userId, Integer commentId) {
        Response response = new Response();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("userId", userId);
        map.put("commentId", commentId);
        if (1 != answerCommentMapper.deleteAnswerComment(map)) {
            throw new MySQLException(MySQLError.DELETE_ERROR, "删除回答评论");
        }
        // 删除评论条数
        answerMapper.deleteAnswerCommentCount(commentId);
        response.setMessage("删除成功");
        return response;

    }

    @Override
    public Response getAnswerCommentById(Integer userId, Integer questionId, Integer answerId, Integer commentId) {
        Response response = new Response();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("questionId", questionId);
        map.put("answerId", answerId);
        map.put("commentId", commentId);
        Map<String, Object> m = answerCommentMapper.getAnswerCommentById(map);

        if (m == null)
            throw new NotFoundException();

        String answerCommentId = String.valueOf(m.get("commentId"));
        if ((Integer) m.get("isDelete") == 1) {
            m.remove("content");
        } else {
            m.put("countLike", redisUtil.hincrby(ANSWER_COMMENT_PREFIX + answerCommentId, ANSWER_COMMENT_COUNT_LIKE_KEY, 0));
            m.put("isLike", socialDao.isLikeAnswerComment(userId, Integer.parseInt(answerCommentId)));
            m.remove("isDelete");
        }
        Integer atUserId = (Integer) m.get("atUserId");
        if (atUserId != null) {
            m.put("atNickname", userMapper.getNickname(atUserId));
        }

        List<Map<String, Object>> nlist = new ArrayList<Map<String, Object>>();
        nlist.add(m);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", nlist);
        result.put("end", 1);
        response.setData(result);
        return response;
    }

    @Override
    public Response likeAnswerComment(Integer userId, Integer commentId) {
        Response response = new Response();
        // 通知MongoDB
        Map<String, Object> map = answerCommentMapper.getNotificationInfo(commentId);

        NotificationData notificationData = new NotificationData();
        notificationData.setQ_id((Integer) map.get("q_id"));
        notificationData.setTitle((String) map.get("title"));
        notificationData.setAc_id((Integer) map.get("ac_id"));
        notificationDao.saveMessage(userId, userMapper.getNickname(userId), (Integer) map.get("at_user_id"), (String) map.get("at_username"), NotificationType.LIKE_COMMENT_ANSWER, notificationData);
        // 写入MongoDB点赞列表
        socialDao.likeAnswerComment(userId, commentId);
        // 写入Redis
        Integer countLike = redisUtil.hincrby(ANSWER_COMMENT_PREFIX + commentId, ANSWER_COMMENT_COUNT_LIKE_KEY, 1);
        response.setData(countLike);
        return response;
    }

    @Override
    public Response unLikeAnswerComment(Integer userId, Integer commentId) {
        Response response = new Response();
        // 删除Redis
        Integer countLike = redisUtil.hincrby(ANSWER_COMMENT_PREFIX + commentId, ANSWER_COMMENT_COUNT_LIKE_KEY, -1);
        // 删除通知MongoDB
        Map<String, Object> map = answerCommentMapper.getNotificationInfo(commentId);
        NotificationData notificationData = new NotificationData();
        notificationData.setQ_id((Integer) map.get("q_id"));
        notificationData.setAc_id((Integer) map.get("ac_id"));
        notificationDao.deleteMessage(userId, (Integer) map.get("at_user_id"), NotificationType.LIKE_COMMENT_ANSWER, notificationData);
        // 删除MongoDB点赞列表
        socialDao.unLikeAnswerComment(userId, commentId);

        response.setData(countLike);
        return response;
    }
}
