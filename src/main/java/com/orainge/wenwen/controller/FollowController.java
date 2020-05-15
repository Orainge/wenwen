package com.orainge.wenwen.controller;

import com.alibaba.druid.util.StringUtils;
import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.exception.NullRequestParametersException;
import com.orainge.wenwen.service.FollowService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @GetMapping("/people")
    @ResponseBody
    public Response apiCheckFollowPeople(@CookieValue("userId") String userId,
                                         String peopleId) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        return followService.apiCheckFollowPeople(Integer.valueOf(userId), Integer.valueOf(peopleId));
    }

    @PostMapping(value = "/people", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiFollowPeople(@CookieValue("userId") String userId,
                                    @RequestBody(required = false) Map<String, Object> map) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        Object[] objs = ControllerHelper.getParametersInObject(map, "userNickname", "peopleId", "peopleNickname");
        String userNickName = (String) objs[0];
        Integer peopleId = (Integer) objs[1];
        String peopleNickname = (String) objs[2];
        return followService.apiFollowPeople(Integer.valueOf(userId), userNickName, peopleId, peopleNickname);
    }

    @DeleteMapping(value = "/people", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiUnFollowPeople(@CookieValue("userId") String userId,
                                      @RequestBody(required = false) Map<String, Integer> map) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        Integer[] ints = ControllerHelper.getParametersInInteger(map, "peopleId");
        return followService.apiUnFollowPeople(Integer.valueOf(userId), ints[0]);
    }

    @GetMapping("/question")
    @ResponseBody
    public Response apiCheckFollowQuestion(@CookieValue("userId") String userId, String questionId) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        return followService.apiCheckFollowQuestion(Integer.valueOf(userId), Integer.valueOf(questionId));
    }

    @PostMapping(value = "/question", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiFollowQuestion(@CookieValue("userId") String userId,
                                      @RequestBody(required = false) Map<String, Object> map) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        Object[] objs = ControllerHelper.getParametersInObject(map, "questionId");
        return followService.apiFollowQuestion(Integer.valueOf(userId), (Integer) objs[0]);
    }

    @DeleteMapping(value = "/question", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiUnFollowQuestion(@CookieValue("userId") String userId,
                                        @RequestBody(required = false) Map<String, Integer> map) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "userId");
        Integer[] ints = ControllerHelper.getParametersInInteger(map, "questionId");
        return followService.apiUnFollowQuestion(Integer.valueOf(userId), ints[0]);
    }
}
