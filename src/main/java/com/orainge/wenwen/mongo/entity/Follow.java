package com.orainge.wenwen.mongo.entity;

import java.io.Serializable;
import java.util.List;

public class Follow implements Serializable {
    private Integer user_id;
    private List<Integer> follow_question_id_list;
    private List<Integer> follow_user_id_list;
    private List<Integer> follow_topic_id_list;
    private List<Integer> follow_favourites_id_list;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public List<Integer> getFollow_question_id_list() {
        return follow_question_id_list;
    }

    public void setFollow_question_id_list(List<Integer> follow_question_id_list) {
        this.follow_question_id_list = follow_question_id_list;
    }

    public List<Integer> getFollow_user_id_list() {
        return follow_user_id_list;
    }

    public void setFollow_user_id_list(List<Integer> follow_user_id_list) {
        this.follow_user_id_list = follow_user_id_list;
    }

    public List<Integer> getFollow_topic_id_list() {
        return follow_topic_id_list;
    }

    public void setFollow_topic_id_list(List<Integer> follow_topic_id_list) {
        this.follow_topic_id_list = follow_topic_id_list;
    }

    public List<Integer> getFollow_favourites_id_list() {
        return follow_favourites_id_list;
    }

    public void setFollow_favourites_id_list(List<Integer> follow_favourites_id_list) {
        this.follow_favourites_id_list = follow_favourites_id_list;
    }
}
