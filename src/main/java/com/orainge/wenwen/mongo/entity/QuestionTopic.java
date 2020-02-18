package com.orainge.wenwen.mongo.entity;

import java.io.Serializable;
import java.util.List;

public class QuestionTopic implements Serializable {
    private Integer question_id;
    private Integer user_id;
    private List<Integer> topic_id_list;

    public Integer getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public List<Integer> getTopic_id_list() {
        return topic_id_list;
    }

    public void setTopic_id_list(List<Integer> topic_id_list) {
        this.topic_id_list = topic_id_list;
    }
}
