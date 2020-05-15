package com.orainge.wenwen.mongo.util;

public enum NotificationType {
    ASK(0, 0, "提问"),
    ANSWER(0, 1, "回答"),
    COMMENT_QUESTION(0, 2, "评论问题"),
    COMMENT_ANSWER(0, 3, "评论回答"),
    REPLY_COMMENT_QUESTION(0, 4, "回复问题评论"),
    REPLY_COMMENT_ANSWER(0, 5, "回复回答评论"),
    LIKE_QUESTION(1, 0, "点赞问题"),
    LIKE_ANSWER(1, 1, "点赞回答"),
    LIKE_COMMENT_QUESTION(1, 2, "点赞问题评论"),
    LIKE_COMMENT_ANSWER(1, 3, "点赞回答评论"),
    FOLLOW_USER(2, 0, "关注用户"),
    FOLLOW_TOPIC(2, 1, "关注话题"),
    FOLLOW_QUESTION(2, 2, "关注问题"),
    FOLLOW_FAVOURITES(2, 3, "关注收藏夹"),
    FAVOUR_ANSWER(3, 0, "收藏回答");

    private Integer type;
    private Integer subType;
    private String name;

    private NotificationType(Integer type, Integer subType, String name) {
        this.type = type;
        this.subType = subType;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public Integer getSubType() {
        return subType;
    }

    public String getName() {
        return name;
    }
}
