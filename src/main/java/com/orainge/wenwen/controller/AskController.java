package com.orainge.wenwen.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.exception.NullRequestParametersException;
import com.orainge.wenwen.service.AskService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class AskController {
    @Autowired
    private AskService askService;

    /**
     * 页面: 显示提问的对话框
     */
    @GetMapping(value = "/ask")
    @SuppressWarnings("all")
    public String toAsk(@CookieValue("userId") String userId, Model model) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        Map<String, String> result = askService.toAsk(userId);
        model.addAllAttributes(result);
        return "ask";
    }

    /**
     * API: 发布问题
     */
    @PostMapping(value = "/ask", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiAsk(@RequestBody(required = false) Map<String, Object> map) throws NullRequestParametersException {
        Object[] result = ControllerHelper.getParametersInObject(map, "userId", "title", "content", "anonymous", "topicList");
        Integer userId = Integer.parseInt((String) result[0]);
        String title = (String) result[1];
        String content = (String) result[2];
        Integer anonymous = (Integer) result[3];
        List<String> topicList = JSON.parseArray(JSON.toJSONString(result[4]), String.class);
        return askService.apiAsk(userId, title, content, anonymous, topicList);
    }

    /**
     * API: 提交草稿
     */
    @PostMapping(value = "/ask/draft", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiSaveDraft(@RequestBody(required = false) Map<String, Object> map) throws NullRequestParametersException {
        Object[] result = ControllerHelper.getParametersInObject(map, "userId", "title", "content", "anonymous", "topicList");
        Integer userId = Integer.parseInt((String) result[0]);
        String title = (String) result[1];
        String content = (String) result[2];
        Integer anonymous = (Integer) result[3];
        String topicList = JSON.toJSONString(result[4]);
        return askService.apiSaveDraft(userId, title, content, anonymous, topicList);
    }
}