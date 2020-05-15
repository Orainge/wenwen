package com.orainge.wenwen.mybatis.mapper;

import com.orainge.wenwen.mybatis.entity.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface QuestionMapper {
    public Integer insertQuestion(Question question);

    public Map<String, Object> queryQuestion(@Param("questionId") Integer questionId);

    public Map<String, Object> queryQuestionInCount(@Param("questionId") Integer questionId);

    public List<Map<String, Object>> getPeopleQuestion(@Param("userId") Integer userId, @Param("peopleId") Integer peopleId);

    public List<Map<String, Object>> getPeopleAnswer(@Param("userId") Integer userId, @Param("peopleId") Integer peopleId);

    public List<Map<String, Object>> getPeopleFollowQuestion(Map<String, Object> map);

    public List<Map<String, Object>> getPeopleFollowUser(Map<String, Object> map);

    public int updateRedisInfo(List<Map<String, String>> list);

    public List<Question> searchQuestion(@Param("keyword") String keyword);

    public List<Question> getTopView();

    public List<Question> getTopAnswer();

    public int deleteQuestion(Question question);

    public Map<String, Object> getFollowPeopleInfo(@Param("questionId") Integer questionId);

    public Question getQuestionToEdit(@Param("questionId") Integer questionId);

    public int saveEditQuestion(Question question);

    public Question getQuestionTitle(@Param("questionId") Integer questionId);

    public Question getAskUserId(@Param("questionId") Integer questionId);

    public int addQuestionCommentCount(@Param("questionId") Integer questionId);

    public int addQuestionAnswerCount(@Param("questionId") Integer questionId);

    public int deleteQuestionCommentCount(@Param("commentId") Integer commentId);

    public int deleteQuestionAnswerCount(@Param("answerId") Integer answerId);
}