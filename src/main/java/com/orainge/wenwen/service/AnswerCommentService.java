package com.orainge.wenwen.service;

import com.orainge.wenwen.util.Response;

public interface AnswerCommentService {
    public Response getAnswerComment(Integer userId, Integer answerId, Integer nextPage);

    public Response apiAnswerComment(Integer userId, Integer answerId, String content, Integer questionId, Integer atUserId);

    public Response apiDeleteAnswerComment(Integer userId, Integer answerCommentId);

    public Response getAnswerCommentById(Integer userId, Integer questionId, Integer answerId, Integer commentId);

    public Response likeAnswerComment(Integer userId, Integer commentId);

    public Response unLikeAnswerComment(Integer userId, Integer commentId);
}
