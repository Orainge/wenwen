package com.orainge.wenwen.mybatis.entity;

public class Topic {
    private Integer topicId;

    private String topicName;

    private Integer countQuestion;

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Integer getCountQuestion() {
        return countQuestion;
    }

    public void setCountQuestion(Integer countQuestion) {
        this.countQuestion = countQuestion;
    }
}