package com.orainge.wenwen.service;

import com.orainge.wenwen.util.Response;

import java.util.List;
import java.util.Map;

public interface QuestionService {
    public Map<String, String> toQuestion(Integer questionId, String userId);

    public Response deleteQuestion(Integer questionId, String userId);

    public Response apiGetAnswerById(Integer userId, Integer questionId, Integer answerId);

    public Map<String, Object> toEditQuestion(Integer questionId);

    public Response apiEdit(Integer questionId, String title, String content, Integer anonymous, List<String> topicIdList);
}
