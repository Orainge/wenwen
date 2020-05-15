package com.orainge.wenwen.redis.util;

import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.exception.DefaultException;
import com.orainge.wenwen.mybatis.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
@EnableAsync
@SuppressWarnings("all")
public class RedisTimer {
    private static final Logger logger = LoggerFactory.getLogger(RedisTimer.class);
    private final String QUESTION = "question:";
    private final String QUESTION_COMMIT = "question_commit:";
    private final String ANSWER = "answer:";
    private final String ANSWER_COMMIT = "answer_commit:";
    private final String PEOPLE = "people:";

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private QuestionCommentMapper questionCommentMapper;
    @Autowired
    private AnswerCommentMapper answerCommentMapper;
    @Autowired
    private UserMapper userMapper;

    @Scheduled(fixedDelay = 5 * 60 * 1000) // 5分钟 * 60秒 * 1000毫秒
    public void writebackTask() {
        logger.info("Redis 回写开始");
        saveQuestion();
        saveQuestionCommit();
        saveAnswer();
        saveAnswerCommit();
        saveUser();
        logger.info("Redis 回写结束");
    }

    private void saveQuestion() {
        List<String> keys = getKeys(QUESTION);
        if (keys.size() == 0)
            return;
        List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
        for (String key : keys) {
            String[] keyS = key.split(":");
            Map<String, String> map = redisUtil.hgetAll(key);
            map.put("question_id", keyS[1]);
            objects.add(map);
        }
        questionMapper.updateRedisInfo(objects);
    }

    private void saveQuestionCommit() {
        List<String> keys = getKeys(QUESTION_COMMIT);
        if (keys.size() == 0)
            return;
        List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
        for (String key : keys) {
            String[] keyS = key.split(":");
            Map<String, String> map = redisUtil.hgetAll(key);
            map.put("comment_id", keyS[1]);
            objects.add(map);
        }
        questionCommentMapper.updateRedisInfo(objects);
    }

    private void saveAnswer() {
        List<String> keys = getKeys(ANSWER);
        if (keys.size() == 0)
            return;
        List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
        for (String key : keys) {
            String[] keyS = key.split(":");
            Map<String, String> map = redisUtil.hgetAll(key);
            map.put("answer_id", keyS[1]);
            objects.add(map);
        }
        answerMapper.updateRedisInfo(objects);
    }

    private void saveAnswerCommit() {
        List<String> keys = getKeys(ANSWER_COMMIT);
        List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
        if (keys.size() == 0)
            return;
        for (String key : keys) {
            String[] keyS = key.split(":");
            Map<String, String> map = redisUtil.hgetAll(key);
            map.put("comment_id", keyS[1]);
            objects.add(map);
        }
        answerCommentMapper.updateRedisInfo(objects);
    }

    private void saveUser() {
        List<String> keys = getKeys(PEOPLE);
        List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
        if (keys.size() == 0)
            return;
        for (String key : keys) {
            String[] keyS = key.split(":");
            Map<String, String> map = redisUtil.hgetAll(key);
            map.put("user_id", keyS[1]);
            objects.add(map);
        }
        userMapper.updateRedisInfo(objects);
    }

    private List<String> getKeys(String type) {
        return redisUtil.getKeys(type);
    }
}
