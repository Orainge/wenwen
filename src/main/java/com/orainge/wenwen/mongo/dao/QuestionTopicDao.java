package com.orainge.wenwen.mongo.dao;

import com.orainge.wenwen.mongo.entity.QuestionTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class QuestionTopicDao {
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongo;

    public void save(QuestionTopic questionTopic) {
        mongo.save(questionTopic);
    }
}
