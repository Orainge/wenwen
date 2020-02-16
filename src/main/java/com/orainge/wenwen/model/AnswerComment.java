package com.orainge.wenwen.model;

import java.util.Date;

public class AnswerComment {
    private Integer commentId;

    private Date createTime;

    private Integer answerId;

    private Integer userId;

    private Integer atUserId;

    private Integer countLike;

    private Integer countUnlike;

    private String content;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAtUserId() {
        return atUserId;
    }

    public void setAtUserId(Integer atUserId) {
        this.atUserId = atUserId;
    }

    public Integer getCountLike() {
        return countLike;
    }

    public void setCountLike(Integer countLike) {
        this.countLike = countLike;
    }

    public Integer getCountUnlike() {
        return countUnlike;
    }

    public void setCountUnlike(Integer countUnlike) {
        this.countUnlike = countUnlike;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}