package com.orainge.wenwen.mybatis.mapper;

import com.orainge.wenwen.mybatis.entity.Topic;

import java.util.List;

public interface TopicMapper {
    public List<Topic> insertTopic(List<Topic> list);
}