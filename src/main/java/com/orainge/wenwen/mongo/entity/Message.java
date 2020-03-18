package com.orainge.wenwen.mongo.entity;

import java.util.Date;
import java.util.Map;

public class Message {
    private Integer user_id;
    private Integer at_user_id;
    private Date time;
    private Integer type;
    private Integer s_type;
    private Map<String, Integer> data;

    public Message() {
        time = new Date();
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getAt_user_id() {
        return at_user_id;
    }

    public void setAt_user_id(Integer at_user_id) {
        this.at_user_id = at_user_id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getS_type() {
        return s_type;
    }

    public void setS_type(Integer s_type) {
        this.s_type = s_type;
    }

    public Map<String, Integer> getData() {
        return data;
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
    }

}
