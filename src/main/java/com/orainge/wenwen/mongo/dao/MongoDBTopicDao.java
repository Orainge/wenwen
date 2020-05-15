package com.orainge.wenwen.mongo.dao;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import com.orainge.wenwen.exception.MongoDBException;
import com.orainge.wenwen.exception.type.MongoDBError;
import com.orainge.wenwen.mongo.entity.MongoDBTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MongoDBTopicDao {
    private final String COLLECTION_NAME = "topic";

    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

    public void saveQuestionToTopic(List<Integer> topicIds, Map<String, Object> mongoDBQuestionObject) {
        try {
            for (Integer id : topicIds) {
                Query query = new Query(Criteria.where("topic_id").is(id));
                Update update = new Update();
                update.addToSet("question_list", mongoDBQuestionObject);
                UpdateResult upResult = mongoTemplate.upsert(query, update, MongoDBTopic.class);
            }
        } catch (Exception e) {
            throw new MongoDBException(e, MongoDBError.INSERT_ERROR,"保存问题对应的话题");
        }
    }

    public void find(MongoDBTopic questionTopic) {
        mongoTemplate.save(questionTopic);
//
//        List<JSONObject> re = mongoTemplate
//                .find(new Query(Criteria.where("name").is("一灰灰blog").and("age").is(120)), JSONObject.class,
//                        COLLECTION_NAME);

    }
}
