package com.orainge.wenwen.controller;

import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.exception.InvalidVariableException;
import com.orainge.wenwen.exception.NotFoundException;
import com.orainge.wenwen.exception.NullRequestParametersException;
import com.orainge.wenwen.mybatis.entity.Answer;
import com.orainge.wenwen.mybatis.entity.Question;
import com.orainge.wenwen.mybatis.mapper.AnswerMapper;
import com.orainge.wenwen.service.QuestionService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerMapper answerMapper;

    @GetMapping(value = "/{questionId}")
    @SuppressWarnings("all")
    public String toQuestion(@PathVariable(value = "questionId", required = false) String questionId,
                             @CookieValue(value = "userId", required = false) String userId,
                             Model model) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(questionId, "问题ID");
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        Integer id = null;
        try {
            id = Integer.parseInt(questionId);
        } catch (Exception e) {
            throw new InvalidVariableException("questionId");
        }

        Map<String, String> result = questionService.toQuestion(id, userId);
        if (result == null) {
            throw new NotFoundException("问题ID: " + questionId);
        }

        model.addAttribute("questionId", questionId);
        model.addAllAttributes(result);
        return "question/question";
    }

    @DeleteMapping(value = "/{questionId}")
    @SuppressWarnings("all")
    @ResponseBody
    public Response deleteQuestion(@PathVariable(value = "questionId", required = false) String questionId,
                                   @CookieValue("userId") String userId,
                                   Model model) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(questionId, "问题ID");
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        Integer id = null;
        try {
            id = Integer.parseInt(questionId);
        } catch (Exception e) {
            throw new InvalidVariableException("questionId");
        }
        return questionService.deleteQuestion(id, userId);
    }


    @GetMapping(value = "/{questionId}/answer/{answerId}")
    @SuppressWarnings("all")
    public String toGetAnswerById(@PathVariable(value = "questionId", required = false) String questionId,
                                  @PathVariable(value = "answerId", required = false) String answerId,
                                  @CookieValue("userId") String userId,
                                  Model model) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        ControllerHelper.validVariableIsExist(questionId, "问题ID");
        ControllerHelper.validVariableIsExist(answerId, "回答ID");
        questionService.apiGetAnswerById(Integer.valueOf(userId), Integer.valueOf(questionId), Integer.valueOf(answerId));
        Integer id = null;
        try {
            id = Integer.parseInt(questionId);
        } catch (Exception e) {
            throw new InvalidVariableException("questionId");
        }

        Map<String, String> result = questionService.toQuestion(id, userId);
        if (result == null) {
            throw new NotFoundException("问题ID: " + questionId);
        }

        model.addAttribute("questionId", questionId);
        model.addAttribute("specificAnswer", 1);
        model.addAttribute("answerId", answerId);
        model.addAllAttributes(result);
        return "question/question";
    }

    @GetMapping(value = "/{questionId}/answer/{answerId}/get")
    @SuppressWarnings("all")
    @ResponseBody
    public Response apiGetAnswerById(@PathVariable(value = "questionId", required = false) String questionId,
                                     @PathVariable(value = "answerId", required = false) String answerId,
                                     @CookieValue("userId") String userId,
                                     Model model) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        ControllerHelper.validVariableIsExist(questionId, "问题ID");
        ControllerHelper.validVariableIsExist(answerId, "回答ID");
        return questionService.apiGetAnswerById(Integer.valueOf(userId), Integer.valueOf(questionId), Integer.valueOf(answerId));
    }

    @GetMapping(value = "/edit")
    @SuppressWarnings("all")
    public String toEdit(String questionId, Model model) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(questionId, "问题ID");
        Map<String, Object> map = questionService.toEditQuestion(Integer.valueOf(questionId));
        map.put("questionId", questionId);
        model.addAllAttributes(map);
        return "edit";
    }

    @PostMapping(value = "/edit", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiEdit(@RequestBody(required = false) Map<String, Object> map) throws NullRequestParametersException {
        Object[] result = ControllerHelper.getParametersInObject(map, "questionId", "title", "content", "anonymous", "topicList");
        Integer questionId = (Integer) result[0];
        String title = (String) result[1];
        String content = (String) result[2];
        Integer anonymous = (Integer) result[3];
        List<String> topicList = JSON.parseArray(JSON.toJSONString(result[4]), String.class);
        return questionService.apiEdit(questionId, title, content, anonymous, topicList);
    }

    @GetMapping(value = "/{questionId}/questionComment/{commentId}")
    public String getQuestionComment(@CookieValue("userId") String userId,
                                     @PathVariable("questionId") String questionId,
                                     @PathVariable("commentId") String commentId,
                                     Model model) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(questionId, "questionId");
        ControllerHelper.validVariableIsExist(commentId, "commentId");
        Integer qId, qcId;
        try {
            qId = Integer.parseInt(questionId);
            qcId = Integer.parseInt(commentId);
        } catch (Exception e) {
            throw new NotFoundException();
        }
        Map<String, String> result = questionService.toQuestion(qId, userId);
        if (result == null) {
            throw new NotFoundException("问题ID: " + questionId);
        }
        model.addAttribute("questionId", qId);
        model.addAttribute("questionCommentId", qcId);
        model.addAllAttributes(result);
        return "question/question";
    }

    @GetMapping(value = "/{questionId}/answerComment/{commentId}")
    public String getAnswerComment(@CookieValue("userId") String userId,
                                   @PathVariable("questionId") String questionId,
                                   @PathVariable("commentId") String commentId,
                                   Model model) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(questionId, "questionId");
        ControllerHelper.validVariableIsExist(commentId, "commentId");
        Integer qId, acId;
        try {
            qId = Integer.parseInt(questionId);
            acId = Integer.parseInt(commentId);
        } catch (Exception e) {
            throw new NotFoundException();
        }
        Map<String, String> result = questionService.toQuestion(qId, userId);
        if (result == null) {
            throw new NotFoundException("问题ID: " + questionId);
        }
        Answer answer = answerMapper.getAnswerIdByCommentId(Integer.parseInt(commentId));
        if (answer == null) {
            throw new NotFoundException();
        }
        model.addAttribute("questionId", qId);
        model.addAttribute("answerId", answer.getAnswerId());
        model.addAttribute("answerCommentId", acId);
        model.addAllAttributes(result);
        return "question/question";
    }
}