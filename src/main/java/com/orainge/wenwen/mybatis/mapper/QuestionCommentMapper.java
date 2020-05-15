package com.orainge.wenwen.mybatis.mapper;

import java.util.List;
import java.util.Map;

public interface QuestionCommentMapper {
    public int updateRedisInfo(List<Map<String, String>> list);
}