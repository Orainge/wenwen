package com.orainge.wenwen.mongo.dao;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.UpdateResult;
import com.orainge.wenwen.mongo.entity.MongoDBTopic;
import com.orainge.wenwen.mongo.entity.Notification;
import com.orainge.wenwen.mongo.entity.Social;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SocialDao {
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;
    private final String COLLECTION_NAME = "social";

    public Integer isLikeAnswer(Integer userId, Integer answerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        query.addCriteria(Criteria.where("answer_like_id_list").in(answerId));
        List<Social> temp = mongoTemplate.find(query, Social.class, COLLECTION_NAME);
//        System.err.println("temp: " + JSON.toJSONString(temp));
        if (temp == null || temp.size() == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public void likeAnswer(Integer userId, Integer answerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        Update update = new Update();
        update.addToSet("answer_like_id_list", answerId);
        UpdateResult upResult = mongoTemplate.upsert(query, update, Social.class);
        System.out.println(JSON.toJSONString(upResult));
    }

    public void unLikeAnswer(Integer userId, Integer answerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        Update update = new Update();
        update.pull("answer_like_id_list", answerId);
        UpdateResult upResult = mongoTemplate.upsert(query, update, Social.class);
        System.out.println(JSON.toJSONString(upResult));
    }

    public Integer isLikeQuestionComment(Integer userId, Integer answerCommentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        query.addCriteria(Criteria.where("question_comment_like_id_list").in(answerCommentId));
        List<Social> temp = mongoTemplate.find(query, Social.class, COLLECTION_NAME);
//        System.err.println("temp: " + JSON.toJSONString(temp));
        if (temp == null || temp.size() == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public void likeQuestionComment(Integer userId, Integer questionCommentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        Update update = new Update();
        update.addToSet("question_comment_like_id_list", questionCommentId);
        UpdateResult upResult = mongoTemplate.upsert(query, update, Social.class);
        System.out.println(JSON.toJSONString(upResult));
    }

    public void unLikeQuestionComment(Integer userId, Integer questionCommentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        Update update = new Update();
        update.pull("question_comment_like_id_list", questionCommentId);
        UpdateResult upResult = mongoTemplate.upsert(query, update, Social.class);
        System.out.println(JSON.toJSONString(upResult));
    }

    public Integer isLikeAnswerComment(Integer userId, Integer answerCommentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        query.addCriteria(Criteria.where("answer_comment_like_id_list").in(answerCommentId));
        List<Social> temp = mongoTemplate.find(query, Social.class, COLLECTION_NAME);
//        System.err.println("temp: " + JSON.toJSONString(temp));
        if (temp == null || temp.size() == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public void likeAnswerComment(Integer userId, Integer answerCommentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        Update update = new Update();
        update.addToSet("answer_comment_like_id_list", answerCommentId);
        UpdateResult upResult = mongoTemplate.upsert(query, update, Social.class);
        System.out.println(JSON.toJSONString(upResult));
    }

    public void unLikeAnswerComment(Integer userId, Integer answerCommentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        Update update = new Update();
        update.pull("answer_comment_like_id_list", answerCommentId);
        UpdateResult upResult = mongoTemplate.upsert(query, update, Social.class);
        System.out.println(JSON.toJSONString(upResult));
    }
}
