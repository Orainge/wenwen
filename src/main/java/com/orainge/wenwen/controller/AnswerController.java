package com.orainge.wenwen.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.exception.NullRequestParametersException;
import com.orainge.wenwen.service.AnswerService;
import com.orainge.wenwen.service.AskService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    /**
     * 页面: 显示回答的对话框
     */
    @GetMapping(value = "/answer")
    @SuppressWarnings("all")
    public String toAsk(@CookieValue("userId") String userId, String questionId, Model model) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        ControllerHelper.validVariableIsExist(questionId, "questionId");
        Map<String, String> result = answerService.toAnswer(Integer.parseInt(userId), Integer.parseInt(questionId));
        model.addAllAttributes(result);
        return "answer";
    }

    /**
     * API: 发布回答
     */
    @PostMapping(value = "/answer", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiAsk(@CookieValue("userId") String userId,
                           @RequestBody(required = false) Map<String, Object> map) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        Object[] result = ControllerHelper.getParametersInObject(map, "questionId", "content", "anonymous", "isShort");
        Integer questionId = Integer.parseInt((String) result[0]);
        String content = (String) result[1];
        Integer anonymous = (Integer) result[2];
        Integer isShort = (Integer) result[3];
        return answerService.apiAnswer(Integer.parseInt(userId), questionId, content, anonymous, isShort);
    }

    /**
     * API: 删除回答
     */
    @DeleteMapping(value = "/answer", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiDeleteAnswer(@CookieValue("userId") String userId,
                                    @RequestBody(required = false) Map<String, Integer> map) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        Integer[] result = ControllerHelper.getParametersInInteger(map, "answerId");
        Integer questionId = result[0];
        return answerService.apiDelete(Integer.parseInt(userId), questionId);
    }

    /**
     * API: 提交回答草稿
     */
    @PostMapping(value = "/answer/draft", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiSaveDraft(@CookieValue("userId") String userId,
                                 @RequestBody(required = false) Map<String, Object> map) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        Object[] result = ControllerHelper.getParametersInObject(map, "questionId", "content", "anonymous", "isShort");
        Integer questionId = Integer.parseInt((String) result[0]);
        String content = (String) result[1];
        Integer anonymous = (Integer) result[2];
        Integer isShort = (Integer) result[3];
        return answerService.apiSaveDraft(Integer.parseInt(userId), questionId, content, anonymous, isShort);
    }

    @GetMapping(value = "/answer/get")
    @SuppressWarnings("all")
    @ResponseBody
    public Response apiGetAnswer(@CookieValue("userId") String userId,
                                 Integer questionId, Integer nextPage) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        ControllerHelper.validVariableIsExist(questionId, "问题ID");
        ControllerHelper.validVariableIsExist(nextPage, "页数");
        return answerService.apiGetAnswer(questionId, userId, nextPage);
    }

    /**
     * API: 点赞
     */
    @PostMapping(value = "/answer/like", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiLikeAnswer(@CookieValue("userId") String userId,
                                  @RequestBody(required = false) Map<String, Integer> map) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        Integer[] result = ControllerHelper.getParametersInInteger(map, "answerId");
        return answerService.apiLikeAnswer(Integer.parseInt(userId), result[0]);
    }

    /**
     * API: 取消点赞
     */
    @DeleteMapping(value = "/answer/like", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiUnLikeAnswer(@CookieValue("userId") String userId,
                                    @RequestBody(required = false) Map<String, Integer> map) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        Integer[] result = ControllerHelper.getParametersInInteger(map, "answerId");
        return answerService.apiUnLikeAnswer(Integer.parseInt(userId), result[0]);
    }
}