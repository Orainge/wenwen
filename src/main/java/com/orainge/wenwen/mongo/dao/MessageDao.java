package com.orainge.wenwen.mongo.dao;

import com.orainge.wenwen.mongo.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageDao {
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongo;

    public void save(Message message) {
        mongo.save(message);
    }

    public List<Message> readTest() {
        return mongo.findAll(Message.class);
    }

}
