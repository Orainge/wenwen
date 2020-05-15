package com.orainge.wenwen.controller;

import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.exception.NullRequestParametersException;
import com.orainge.wenwen.service.NotificationService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value = "/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = "/lite/feed")
    @ResponseBody
    public Response toLiteFeed(@CookieValue(value = "userId", required = false) Integer userId,
                               Integer page) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(page, "需要获取通知的页数");
        if (page < 1)
            throw new NullRequestParametersException();
        return notificationService.toLiteFeed(userId, page);
    }

    @GetMapping(value = "/lite/message")
    @ResponseBody
    public Response toLiteMessage(@CookieValue(value = "userId", required = false) Integer userId,
                                  Integer page) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(page, "需要获取通知的页数");
        if (page < 1)
            throw new NullRequestParametersException();
        return notificationService.toLiteMessage(userId, page);
    }

    @GetMapping(value = "/lite/like")
    @ResponseBody
    public Response toLiteLike(@CookieValue(value = "userId", required = false) Integer userId,
                               Integer page) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(page, "需要获取通知的页数");
        if (page < 1)
            throw new NullRequestParametersException();
        return notificationService.toLiteLike(userId, page);
    }
}