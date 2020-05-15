package com.orainge.wenwen.controller;

import com.alibaba.druid.util.StringUtils;
import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.exception.NullRequestParametersException;
import com.orainge.wenwen.service.AnswerCommentService;
import com.orainge.wenwen.service.AnswerService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/answerComment")
public class AnswerCommentController {
    @Autowired
    private AnswerCommentService answerCommentService;

    @GetMapping
    @ResponseBody
    public Response getAnswerComment(@CookieValue("userId") String userId, String answerId, String nextPage) {
        ControllerHelper.validVariableIsExist(answerId, "answerId");
        ControllerHelper.validVariableIsExist(nextPage, "nextPage");
        return answerCommentService.getAnswerComment(Integer.parseInt(userId), Integer.parseInt(answerId), Integer.parseInt(nextPage));
    }

    @PostMapping(produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiAnswerComment(@CookieValue("userId") String userId,
                                     @RequestBody(required = false) Map<String, Object> map) throws NullRequestParametersException {
        Object[] result = ControllerHelper.getParametersInObject(map, "answerId", "content", "questionId");
        return answerCommentService.apiAnswerComment(Integer.valueOf(userId), (Integer) result[0], (String) result[1], (Integer) result[2], (Integer) map.get("atUserId"));
    }

    @DeleteMapping(produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiDeleteAnswerComment(@CookieValue("userId") String userId,
                                           @RequestBody(required = false) Map<String, Object> map) throws NullRequestParametersException {
        Object[] result = ControllerHelper.getParametersInObject(map, "answerCommentId");
        return answerCommentService.apiDeleteAnswerComment(Integer.valueOf(userId), (Integer) result[0]);
    }

    @GetMapping(value = "/{commentId}")
    @ResponseBody
    public Response getAnswerCommentById(@CookieValue("userId") String userId,
                                         @PathVariable("commentId") String commentId,
                                         String questionId, String answerId) {
        ControllerHelper.validVariableIsExist(commentId, "commentId");
        ControllerHelper.validVariableIsExist(questionId, "questionId");
        ControllerHelper.validVariableIsExist(answerId, "answerId");
        return answerCommentService.getAnswerCommentById(Integer.parseInt(userId), Integer.parseInt(questionId), Integer.parseInt(answerId), Integer.parseInt(commentId));
    }

    @PostMapping(value = "/like", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response likeAnswerComment(@CookieValue("userId") String userId, @RequestBody(required = false) Map<String, Object> map) throws NullRequestParametersException {
        Object[] result = ControllerHelper.getParametersInObject(map, "answerCommentId");
        return answerCommentService.likeAnswerComment(Integer.parseInt(userId), (Integer) result[0]);

    }

    @DeleteMapping(value = "/like", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response unlikeAnswerComment(@CookieValue("userId") String userId, @RequestBody(required = false) Map<String, Object> map) throws NullRequestParametersException {
        Object[] result = ControllerHelper.getParametersInObject(map, "answerCommentId");
        return answerCommentService.unLikeAnswerComment(Integer.parseInt(userId), (Integer) result[0]);
    }

    @GetMapping(value = "/show")
    public String toPage(String questionId, String answerId, String commentId, Model model) throws NullRequestParametersException {
        model.addAttribute("questionId", questionId);
        model.addAttribute("answerId", answerId);
        if (!StringUtils.isEmpty(commentId)) {
            model.addAttribute("commentId", commentId);
        }
        return "comment/answer";
    }
}