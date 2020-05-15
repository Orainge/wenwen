package com.orainge.wenwen.mongo.entity;

import java.io.Serializable;
import java.util.Date;

public class Notification implements Serializable {
    private Integer user_id;
    private String username;
    private Integer at_user_id;
    private String at_username;
    private Date time;
    private Integer type;
    private Integer s_type;
    private NotificationData data;
    private Integer is_cancel;
    private Integer is_follow_question; // 0: 不是feed中的“"关注的问题有了新回答", 1: 是...

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAt_user_id() {
        return at_user_id;
    }

    public void setAt_user_id(Integer at_user_id) {
        this.at_user_id = at_user_id;
    }

    public String getAt_username() {
        return at_username;
    }

    public void setAt_username(String at_username) {
        this.at_username = at_username;
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

    public NotificationData getData() {
        return data;
    }

    public void setData(NotificationData data) {
        this.data = data;
    }

    public Integer getIs_follow_question() {
        return is_follow_question;
    }

    public void setIs_follow_question(Integer is_follow_question) {
        this.is_follow_question = is_follow_question;
    }

    public Integer getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(Integer is_cancel) {
        this.is_cancel = is_cancel;
    }
}
