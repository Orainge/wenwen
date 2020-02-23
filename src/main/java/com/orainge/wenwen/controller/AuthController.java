package com.orainge.wenwen.controller;

import com.alibaba.druid.util.StringUtils;
import com.orainge.wenwen.service.AuthService;
import com.orainge.wenwen.util.ControllerHelper;
import com.orainge.wenwen.util.NullParmatersException;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    /**
     * API: 用户提交注册信息进行注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiRegister(@RequestBody(required = false) Map<String, String> map) throws NullParmatersException {
        String[] result = ControllerHelper.getMapParamtersInString(map, "email", "username", "password");
        return authService.apiRegister(result[0], result[1], result[2]);
    }

    /**
     * 页面: 要求发送激活链接
     */
    @RequestMapping(value = "/sendActivate", method = RequestMethod.GET)
    public String toSendActivate() {
        return "auth/register/sendActivate";
    }

    /**
     * API: 发送激活邮件到指定邮箱
     */
    @RequestMapping(value = "/sendActivate", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiSendActivate(@RequestBody(required = false) Map<String, String> map) throws NullParmatersException {
        String[] result = ControllerHelper.getMapParamtersInString(map, "email");
        return authService.apiSendActivate(result[0]);
    }

    /**
     * 页面: 要求发送重置密码链接
     */
    @RequestMapping(value = "/sendReset", method = RequestMethod.GET)
    public String toSendReset() {
        return "auth/password/sendReset";
    }

    /**
     * API: 给指定邮箱发送重置密码邮件
     */
    @RequestMapping(value = "/sendReset", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiSendReset(@RequestBody(required = false) Map<String, String> map) throws NullParmatersException {
        String[] result = ControllerHelper.getMapParamtersInString(map, "email");
        return authService.apiSendReset(result[0]);
    }

    /**
     * API: 激活指定邮箱的账户
     */
    @RequestMapping(value = "/activate/{token}", method = RequestMethod.GET)
    public String apiActivate(@PathVariable(value = "token", required = false) String token, Model model) throws NullParmatersException {
        ControllerHelper.validToken(token);
        model.addAttribute("response", authService.apiActivate(token)); // 向前端传送信息 response
        return "auth/register/activate";
    }


    /**
     * 页面：根据 Token 来检查是否有权限重置密码
     */
    @RequestMapping(value = "/resetPassword/{token}", method = RequestMethod.GET)
    public String toResetPassword(@PathVariable(value = "token", required = false) String token, Model model) throws NullParmatersException {
        ControllerHelper.validToken(token);
        model.addAttribute("response", authService.toResetPassword(token)); // 向前端传送信息 response
        return "auth/password/reset";
    }

    /**
     * API: 根据 Token 重置密码
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiResetPassword(@RequestBody(required = false) Map<String, String> map) throws NullParmatersException {
        String[] result = ControllerHelper.getMapParamtersInString(map, "token", "password");
        return authService.apiResetPassword(result[0], result[1]);
    }
}