package com.orainge.wenwen.mongo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Document(collection = "topic")
public class MongoDBTopic implements Serializable {
    private Integer topic_id;
    private Integer user_id;
    private List<Map<String, Object>> question_list;

    public Integer getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(Integer topic_id) {
        this.topic_id = topic_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public List<Map<String, Object>> getQuestion_list() {
        return question_list;
    }

    public void setQuestion_list(List<Map<String, Object>> question_list) {
        this.question_list = question_list;
    }
}
