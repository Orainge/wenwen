package com.orainge.wenwen.mongo.entity;

import java.io.Serializable;
import java.util.List;

public class Favourites implements Serializable {
    private Integer favourite_id;
    private Integer user_id;
    private List<Integer> answer_id_list;

    public Integer getFavourite_id() {
        return favourite_id;
    }

    public void setFavourite_id(Integer favourite_id) {
        this.favourite_id = favourite_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public List<Integer> getAnswer_id_list() {
        return answer_id_list;
    }

    public void setAnswer_id_list(List<Integer> answer_id_list) {
        this.answer_id_list = answer_id_list;
    }
}
