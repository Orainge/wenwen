package com.orainge.wenwen.redis.util;

import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.mybatis.mapper.*;
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
    public void RewriteTask() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("回写开始: " + sdf.format(new Date()));
        saveQuestion();
        saveQuestionCommit();
        saveAnswer();
        saveAnswerCommit();
        saveUser();
        System.out.println("回写结束: " + sdf.format(new Date()));
    }

    private void saveQuestion() {
        System.out.println("saveQuestion: start");
        List<String> keys = getKeys(QUESTION);
        System.out.println("Question:" + JSON.toJSONString(keys));
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
        System.out.println("saveQuestionCommit: start");
        List<String> keys = getKeys(QUESTION_COMMIT);
        System.out.println("saveQuestionCommit:" + JSON.toJSONString(keys));
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
        System.out.println("saveAnswer: start");
        List<String> keys = getKeys(ANSWER);
        System.out.println("saveAnswer:" + JSON.toJSONString(keys));
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
        System.out.println("saveAnswerCommit: start");
        List<String> keys = getKeys(ANSWER_COMMIT);
        System.out.println("saveAnswerCommit:" + JSON.toJSONString(keys));
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
        System.out.println("saveUser: start");
        List<String> keys = getKeys(PEOPLE);
        System.out.println("saveUser:" + JSON.toJSONString(keys));
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
