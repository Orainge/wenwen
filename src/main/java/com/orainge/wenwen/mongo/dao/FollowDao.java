package com.orainge.wenwen.mongo.dao;

import com.orainge.wenwen.mongo.entity.Follow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class FollowDao {
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongo;

    public void save(Follow follow) {
        mongo.save(follow);
    }

}
