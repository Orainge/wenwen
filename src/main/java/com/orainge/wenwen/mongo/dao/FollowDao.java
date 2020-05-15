package com.orainge.wenwen.mongo.dao;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.UpdateResult;
import com.orainge.wenwen.exception.MongoDBException;
import com.orainge.wenwen.exception.type.MongoDBError;
import com.orainge.wenwen.mongo.entity.Follow;
import com.orainge.wenwen.mongo.entity.MongoDBTopic;
import com.orainge.wenwen.mongo.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class FollowDao {
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;
    private final String collectionName = "follow";

    public Integer getFollowQuestionCount(Integer userId) {
        Integer count = 0; //查询不到就返回0
        Criteria criteria = Criteria.where("user_id").is(userId);
        Query query = new Query().addCriteria(criteria);
        query.fields().include("follow_question_list");
        List<Follow> followList = mongoTemplate.find(query, Follow.class, collectionName);
        if (followList.size() != 0) {
            if (followList.get(0).getFollow_question_list() != null) {
                count = followList.get(0).getFollow_question_list().size();
            }
        }
        return count;
    }

    public List<Integer> getFollowQuestionIds(Integer userId) {
        Criteria criteria = Criteria.where("user_id").is(userId);
        Query query = new Query().addCriteria(criteria);
        query.fields().include("follow_question_list");
        List<Follow> followList = mongoTemplate.find(query, Follow.class, collectionName);
        if (followList == null || followList.size() == 0 || followList.get(0).getFollow_question_list() == null || followList.get(0).getFollow_question_list().size() == 0) {
            return null;
        }
        return followList.get(0).getFollow_question_list();
    }

    public List<Integer> getFollowUserIds(Integer userId) {
        Criteria criteria = Criteria.where("user_id").is(userId);
        Query query = new Query().addCriteria(criteria);
        query.fields().include("follow_user_list");
        List<Follow> followList = mongoTemplate.find(query, Follow.class, collectionName);
        if (followList == null || followList.size() == 0 || followList.get(0).getFollow_user_list() == null || followList.get(0).getFollow_user_list().size() == 0) {
            return null;
        }
        return followList.get(0).getFollow_user_list();
    }

    public boolean isFollowPeople(Integer userId, Integer peopleId) {
        Criteria criteria1 = Criteria.where("user_id").is(userId);
        Criteria criteria2 = Criteria.where("follow_user_list").in(peopleId);
        Query query = new Query().addCriteria(criteria1);
        query.addCriteria(criteria2);
        List<Follow> list = mongoTemplate.find(query, Follow.class, collectionName);
        return !(list == null || list.size() == 0);
    }

    public void followPeople(Integer userId, Integer peopleId) {
        try {
            Query query = new Query(Criteria.where("user_id").is(userId));
            Update update = new Update();
            update.addToSet("follow_user_list", peopleId);
            UpdateResult upResult = mongoTemplate.upsert(query, update, Follow.class);
        } catch (Exception e) {
            throw new MongoDBException(e, MongoDBError.INSERT_ERROR,"关注用户");
        }
    }

    public void unFollowPeople(Integer userId, Integer peopleId) {
        try {
            Query query = new Query(Criteria.where("user_id").is(userId));
            Update update = new Update();
            update.pull("follow_user_list", peopleId);
            UpdateResult upResult = mongoTemplate.updateFirst(query, update, Follow.class);
        } catch (Exception e) {
            throw new MongoDBException(e, MongoDBError.DELETE_ERROR,"解除关注用户");
        }
    }

    public boolean isFollowQuestion(Integer userId, Integer questionId) {
        Criteria criteria1 = Criteria.where("user_id").is(userId);
        Criteria criteria2 = Criteria.where("follow_question_list").in(questionId);
        Query query = new Query().addCriteria(criteria1);
        query.addCriteria(criteria2);
        List<Follow> list = mongoTemplate.find(query, Follow.class, collectionName);
        return !(list == null || list.size() == 0);
    }

    public void followQuestion(Integer userId, Integer peopleId) {
        try {
            Query query = new Query(Criteria.where("user_id").is(userId));
            Update update = new Update();
            update.addToSet("follow_question_list", peopleId);
            UpdateResult upResult = mongoTemplate.upsert(query, update, Follow.class);
        } catch (Exception e) {
            throw new MongoDBException(e, MongoDBError.INSERT_ERROR,"关注问题");
        }
    }

    public void unFollowQuestion(Integer userId, Integer peopleId) {
        try {
            Query query = new Query(Criteria.where("user_id").is(userId));
            Update update = new Update();
            update.pull("follow_question_list", peopleId);
            UpdateResult upResult = mongoTemplate.updateFirst(query, update, Follow.class);
        } catch (Exception e) {
            throw new MongoDBException(e, MongoDBError.INSERT_ERROR,"解除关注问题");
        }
    }
}
