package com.orainge.wenwen.mybatis.mapper;

import com.orainge.wenwen.mybatis.entity.AnswerComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AnswerCommentMapper {
    public int updateRedisInfo(List<Map<String, String>> list);

    public List<Map<String, Object>> getAnswerComment(@Param("answerId") Integer answerId);

    public int saveAnswerComment(AnswerComment answerComment);

    public Map<String, Object> getNotificationInfo(@Param("commentId") Integer commentId);

    public Integer getUserIdByCommentId(@Param("commentId") Integer commentId);

    public int deleteAnswerComment(Map<String, Integer> map);

    public Map<String, Object> getAnswerCommentById(Map<String, Integer> map);
}