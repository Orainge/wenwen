package com.orainge.wenwen.mybatis.entity;

import java.util.Date;

public class Answer {
    private Integer answerId;

    private Date createTime;

    private Integer questionId;

    private Integer userId;

    private Integer countLike;

    private Integer countCommit;

    private Integer anonymous;

    private String content;

    private Integer isDelete;

    private Integer isShort;

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCountLike() {
        return countLike;
    }

    public void setCountLike(Integer countLike) {
        this.countLike = countLike;
    }

    public Integer getCountCommit() {
        return countCommit;
    }

    public void setCountCommit(Integer countCommit) {
        this.countCommit = countCommit;
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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getIsShort() {
        return isShort;
    }

    public void setIsShort(Integer isShort) {
        this.isShort = isShort;
    }
}