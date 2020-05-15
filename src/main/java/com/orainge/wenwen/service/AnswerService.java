package com.orainge.wenwen.service;

import com.orainge.wenwen.util.Response;

import java.util.List;
import java.util.Map;

public interface AnswerService {
    public Map<String, String> toAnswer(Integer userId, Integer questionId);

    public Response apiAnswer(Integer userId, Integer questionId, String content, Integer anonymous, Integer isShort);

    public Response apiSaveDraft(Integer userId, Integer questionId, String content, Integer anonymous, Integer isShort);

    public Response apiDelete(Integer userId, Integer questionId);

    public Response apiGetAnswer(Integer questionId, String userId, Integer nextPage);

    public Response apiLikeAnswer(Integer userId, Integer answerId);

    public Response apiUnLikeAnswer(Integer userId, Integer answerId);
}
