package com.orainge.wenwen.controller;

import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.controller.util.NullParametersException;
import com.orainge.wenwen.service.SearchService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class SearchController {
    @Autowired
    SearchService searchService;

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String toSearch(Map<String, String> map, Model model) throws NullParametersException {
        String[] result =  ControllerHelper.getParametersInString(map,"content");
        Response response = searchService.toSearch(result[0]);
        model.addAttribute("code", response.getCode());
        if (response.getCode() == 0) {
            model.addAttribute("data", response.getData());
        } else {
            model.addAttribute("message", response.getMessage());
        }
        return "search";
    }
}