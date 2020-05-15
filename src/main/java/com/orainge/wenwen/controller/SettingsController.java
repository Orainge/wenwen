package com.orainge.wenwen.controller;

import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.exception.NotFoundException;
import com.orainge.wenwen.exception.NullRequestParametersException;
import com.orainge.wenwen.mybatis.entity.User;
import com.orainge.wenwen.service.SettingsService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value = "/settings")
public class SettingsController {
    @Autowired
    private SettingsService settingsService;

    @GetMapping
    public String toDefault() {
        return "redirect:settings/profile";
    }

    @GetMapping("/profile")
    public String toProfile(@CookieValue(value = "userId", required = false) String userId, Model model) {
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        Map<String, Object> result = settingsService.toProfile(userId);
        if (result == null || result.size() == 0) {
            throw new NotFoundException();
        }
        model.addAllAttributes(result);
        return "settings/profile";
    }

    @GetMapping("/password")
    public String toPassword() {
        return "settings/password";
    }

    @GetMapping("/avatar")
    public String toAvatar() {
        return "settings/avatar";
    }

    @PostMapping(value = "/profile", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiProfile(@RequestBody(required = false) User user) throws NullRequestParametersException {
        if (user == null) {
            throw new NullRequestParametersException("用户信息");
        }
        Response result = settingsService.apiProfile(user);
        return result;
    }

    @PostMapping(value = "/password", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiPassword(@CookieValue(value = "userId", required = false) String userId,
                                @RequestBody(required = false) Map<String, String> map) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        String[] result = ControllerHelper.getParametersInString(map, "oldPassword", "newPassword");
        return settingsService.apiPassword(userId, map);
    }

    @PostMapping(value = "/avatar", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiAvatar(@CookieValue(value = "userId", required = false) String userId,
                              @RequestBody(required = false) Map<String, String> map) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        String[] result = ControllerHelper.getParametersInString(map, "base64Data");
        return settingsService.apiAvatar(Integer.valueOf(userId), result[0]);
    }
}
