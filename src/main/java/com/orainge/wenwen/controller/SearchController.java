package com.orainge.wenwen.controller;

import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.exception.NullRequestParametersException;
import com.orainge.wenwen.service.SearchService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    SearchService searchService;

    @GetMapping
    public String toSearch(String content, Model model) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(content, "content");
        model.addAttribute("keyword", content);
        return "search";
    }

    @GetMapping("/question")
    @ResponseBody
    public Response apiSearchQuestion(String keyword, Integer nextPage) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(keyword, "keyword");
        ControllerHelper.validVariableIsExist(nextPage, "nextPage");
        return searchService.apiSearchQuestion(keyword, nextPage);
    }

    @GetMapping("/user")
    @ResponseBody
    public Response apiSearchUser(String keyword, Integer nextPage) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(keyword, "keyword");
        ControllerHelper.validVariableIsExist(nextPage, "nextPage");
        return searchService.apiSearchUser(keyword, nextPage);
    }
}