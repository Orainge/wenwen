package com.orainge.wenwen.mybatis.entity;

import java.util.Date;

public class Question {
    private Integer questionId;

    private String title;

    private Integer userId;

    private Integer countFollow;

    private Integer countBrowse;

    private Integer countCommit;

    private Integer countAnswer;

    private Date createTime;

    private Integer anonymous;

    private String content;

    private String topicList;

    private Integer isDelete;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCountFollow() {
        return countFollow;
    }

    public void setCountFollow(Integer countFollow) {
        this.countFollow = countFollow;
    }

    public Integer getCountBrowse() {
        return countBrowse;
    }

    public void setCountBrowse(Integer countBrowse) {
        this.countBrowse = countBrowse;
    }

    public Integer getCountCommit() {
        return countCommit;
    }

    public void setCountCommit(Integer countCommit) {
        this.countCommit = countCommit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Integer anonymous) {
        this.anonymous = anonymous;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopicList() {
        return topicList;
    }

    public void setTopicList(String topicList) {
        this.topicList = topicList;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getCountAnswer() {
        return countAnswer;
    }

    public void setCountAnswer(Integer countAnswer) {
        this.countAnswer = countAnswer;
    }
}