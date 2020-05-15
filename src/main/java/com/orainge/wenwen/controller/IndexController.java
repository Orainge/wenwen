package com.orainge.wenwen.controller;

import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.exception.NullRequestParametersException;
import com.orainge.wenwen.service.IndexService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {
    @Autowired
    private IndexService indexService;

    @GetMapping("/")
    public String toIndex() {
        return "index";
    }


    @GetMapping("/index/topView")
    @ResponseBody
    public Response apiTopView(Integer nextPage) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(nextPage, "nextPage");
        return indexService.apiTopView(nextPage);
    }

    @GetMapping("/index/topAnswer")
    @ResponseBody
    public Response apiTopAnswer(Integer nextPage) throws NullRequestParametersException {
        ControllerHelper.validVariableIsExist(nextPage, "nextPage");
        return indexService.apiTopAnswer(nextPage);
    }
}