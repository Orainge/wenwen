package com.orainge.wenwen.mybatis.mapper;

import com.orainge.wenwen.mybatis.entity.Answer;
import com.orainge.wenwen.mybatis.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AnswerMapper {
    public int insertAnswer(Answer answer);

    public int updateRedisInfo(List<Map<String, String>> list);

    public int deleteAnswer(Answer answer);

    public List<Map<String, Object>> getAnswerByQuestionId(Answer answer);

    public Map<String, Object> getAnswerInfoByAnswerId(@Param("answerId") Integer answerId);

    public Map<String, Object> getAnswerById(Answer answer);

    public Answer getAnswerUserId(@Param("answerId") Integer answerId);

    public int addAnswerCommentCount(@Param("answerId") Integer answerId);

    public int deleteAnswerCommentCount(@Param("commentId") Integer commentId);

    public Answer getAnswerIdByCommentId(@Param("commentId") Integer commentId);
}