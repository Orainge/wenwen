package com.orainge.wenwen.mongo.entity;

import java.io.Serializable;

public class NotificationData implements Serializable {
    private Integer q_id;
    private Integer a_id;
    private Integer qc_id;
    private Integer ac_id;
    private String title;

    public Integer getQ_id() {
        return q_id;
    }

    public void setQ_id(Integer q_id) {
        this.q_id = q_id;
    }

    public Integer getA_id() {
        return a_id;
    }

    public void setA_id(Integer a_id) {
        this.a_id = a_id;
    }

    public Integer getQc_id() {
        return qc_id;
    }

    public void setQc_id(Integer qc_id) {
        this.qc_id = qc_id;
    }

    public Integer getAc_id() {
        return ac_id;
    }

    public void setAc_id(Integer ac_id) {
        this.ac_id = ac_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
