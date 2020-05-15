package com.orainge.wenwen.mongo.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Follow implements Serializable {
    private Integer user_id;
    private List<Integer> follow_question_list;
    private List<Integer> follow_user_list;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public List<Integer> getFollow_question_list() {
        return follow_question_list;
    }

    public void setFollow_question_list(List<Integer> follow_question_list) {
        this.follow_question_list = follow_question_list;
    }

    public List<Integer> getFollow_user_list() {
        return follow_user_list;
    }

    public void setFollow_user_list(List<Integer> follow_user_list) {
        this.follow_user_list = follow_user_list;
    }

}
