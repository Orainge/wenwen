package com.orainge.wenwen.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.exception.NotFoundException;
import com.orainge.wenwen.service.PeopleService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/people")
public class PeopleController {
    @Autowired
    private PeopleService peopleService;

    @GetMapping
    public String toPeople(@CookieValue(value = "userId", required = false) String userId) {
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        return "redirect:people/" + userId;
    }

    @GetMapping(value = "/to/{redirectTab}")
    public String toPeopleRedirect(@CookieValue(value = "userId", required = false) String userId,
                                   @PathVariable(value = "redirectTab") String redirectTab,
                                   RedirectAttributes attributes) {
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        ControllerHelper.validVariableIsExist(redirectTab, "redirectTab");
        String tab = null;
        switch (redirectTab) {
            case "question":
                tab = "question";
                break;
            case "answer":
                tab = "answer";
                break;
            case "followQuestion":
                tab = "followQuestion";
                break;
            case "followUser":
                tab = "followUser";
                break;
        }
        attributes.addFlashAttribute("tab", tab);
        return "redirect:/people/" + userId;
    }

    @GetMapping("/{peopleId}")
    public String toPeople(@CookieValue(value = "userId", required = false) String userId,
                           @PathVariable(value = "peopleId", required = false) String peopleId,
                           @ModelAttribute("tab") String tab,
                           Model model) {
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        ControllerHelper.validVariableIsExist(peopleId, "peopleId");
        try {
            Integer.valueOf(peopleId);
        } catch (Exception e) {
            throw new NotFoundException();
        }
        Map<String, Object> result = peopleService.toPeople(peopleId);
        if (result == null || result.size() == 0) {
            throw new NotFoundException();
        }
        model.addAllAttributes(result);
        if (!StringUtils.isEmpty(tab))
            model.addAttribute("redirectTab", tab);
        return "people/people";
    }

    @GetMapping("/get/question")
    @ResponseBody
    public Response apiQuestion(@CookieValue(value = "userId", required = false) String userId, Integer peopleId, Integer nextPage) {
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        ControllerHelper.validVariableIsExist(peopleId, "peopleId");
        ControllerHelper.validVariableIsExist(nextPage, "nextPage");
        return peopleService.apiQuestion(Integer.valueOf(userId), peopleId, nextPage);
    }

    @GetMapping("/get/answer")
    @ResponseBody
    public Response apiAnswer(@CookieValue(value = "userId", required = false) String userId, Integer peopleId, Integer nextPage) {
        ControllerHelper.validVariableIsExist(userId, "用户ID");
        ControllerHelper.validVariableIsExist(peopleId, "peopleId");
        ControllerHelper.validVariableIsExist(nextPage, "nextPage");
        return peopleService.apiAnswer(Integer.valueOf(userId), peopleId, nextPage);
    }

    @GetMapping("/get/followQuestion")
    @ResponseBody
    public Response apiFollowQuestion(Integer peopleId, Integer nextPage) {
        ControllerHelper.validVariableIsExist(peopleId, "peopleId");
        ControllerHelper.validVariableIsExist(nextPage, "nextPage");
        return peopleService.apiFollowQuestion(peopleId, nextPage);
    }

    @GetMapping("/get/followUser")
    @ResponseBody
    public Response apiFollowUser(Integer peopleId, Integer nextPage) {
        ControllerHelper.validVariableIsExist(peopleId, "peopleId");
        ControllerHelper.validVariableIsExist(nextPage, "nextPage");
        return peopleService.apiFollowUser(peopleId, nextPage);
    }
}
