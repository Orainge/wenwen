package com.orainge.wenwen.mongo.dao;

import com.orainge.wenwen.mongo.entity.Favourites;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class FavouritesDao {
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongo;

    public void save(Favourites favourites) {
        mongo.save(favourites);
    }



}
