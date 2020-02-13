package com.orainge.wenwen.controller;

import com.orainge.wenwen.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @RequestMapping("/login")
    public String toLogin() {
        return "login";
    }
}
