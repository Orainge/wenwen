package com.orainge.wenwen.controller;

import com.orainge.wenwen.controller.util.ControllerHelper;
import com.orainge.wenwen.service.AuthService;
import com.orainge.wenwen.controller.util.NullParametersException;
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
    public Response apiRegister(@RequestBody(required = false) Map<String, String> map) throws NullParametersException {
        String[] result = ControllerHelper.getParametersInString(map, "email", "username", "password");
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
    public Response apiSendActivate(@RequestBody(required = false) Map<String, String> map) throws NullParametersException {
        String[] result = ControllerHelper.getParametersInString(map, "email");
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
    public Response apiSendReset(@RequestBody(required = false) Map<String, String> map) throws NullParametersException {
        String[] result = ControllerHelper.getParametersInString(map, "email");
        return authService.apiSendReset(result[0]);
    }

    /**
     * API: 激活指定邮箱的账户
     */
    @RequestMapping(value = "/activate/{token}", method = RequestMethod.GET)
    public String apiActivate(@PathVariable(value = "token", required = false) String token, Model model) throws NullParametersException {
        ControllerHelper.validTokenIsExist(token);
        Response response = authService.apiActivate(token);
        model.addAttribute("code", response.getCode());
        if (response.getCode() == 0) {
            model.addAttribute("data", response.getData());
        } else {
            model.addAttribute("message", response.getMessage());
        }
        return "auth/register/activate";
    }


    /**
     * 页面：根据 Token 来检查是否有权限重置密码
     */
    @RequestMapping(value = "/resetPassword/{token}", method = RequestMethod.GET)
    public String toResetPassword(@PathVariable(value = "token", required = false) String token, Model model) throws NullParametersException {
        ControllerHelper.validTokenIsExist(token);
        Response response = authService.toResetPassword(token);
        model.addAttribute("code", response.getCode());
        if (response.getCode() == 0) {
            model.addAttribute("data", response.getData());
        } else {
            model.addAttribute("message", response.getMessage());
        }
        return "auth/password/reset";
    }

    /**
     * API: 根据 Token 重置密码
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Response apiResetPassword(@RequestBody(required = false) Map<String, String> map) throws NullParametersException {
        String[] result = ControllerHelper.getParametersInString(map, "token", "password");
        return authService.apiResetPassword(result[0], result[1]);
    }
}