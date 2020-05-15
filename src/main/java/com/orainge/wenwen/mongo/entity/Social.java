package com.orainge.wenwen.mongo.entity;

import java.io.Serializable;
import java.util.List;

public class Social implements Serializable {
    private Integer user_id;
    private List<Integer> question_comment_like_id_list;
    private List<Integer> answer_like_id_list;
    private List<Integer> answer_comment_like_id_list;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public List<Integer> getQuestion_comment_like_id_list() {
        return question_comment_like_id_list;
    }

    public void setQuestion_comment_like_id_list(List<Integer> question_comment_like_id_list) {
        this.question_comment_like_id_list = question_comment_like_id_list;
    }

    public List<Integer> getAnswer_like_id_list() {
        return answer_like_id_list;
    }

    public void setAnswer_like_id_list(List<Integer> answer_like_id_list) {
        this.answer_like_id_list = answer_like_id_list;
    }

    public List<Integer> getAnswer_comment_like_id_list() {
        return answer_comment_like_id_list;
    }

    public void setAnswer_comment_like_id_list(List<Integer> answer_comment_like_id_list) {
        this.answer_comment_like_id_list = answer_comment_like_id_list;
    }
}
