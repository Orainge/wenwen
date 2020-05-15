package com.orainge.wenwen.shiro.util;

import com.orainge.wenwen.util.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

@Component
public class LoginUtil {
    // 错误提示 状态码对应数组的下标
    private final String[] loginMessage = {"登录成功", "连接错误，请稍后再试", "用户名或密码错误", "用户名或密码错误次数过多", "用户名或密码错误", "账户未激活，请先激活账户"};
    private final String[] logoutMessage = {"退出登录成功", "连接错误，请稍后再试"};

    /**
     * 执行 登录 操作
     *
     * @param principal 用户名或邮箱(两者可以同时传入)
     * @param password  密码
     * @return 登录状态信息
     */
    public Response login(String principal, String password, boolean rememberMe) {
        int code = 5;
        Subject subject = SecurityUtils.getSubject(); // 从SecurityUtils里边创建一个 subject
        UsernamePasswordToken token = new UsernamePasswordToken(principal, password, rememberMe); // 在认证提交前准备 token（令牌）

        /* 执行认证登陆 */
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException ice) {
            /* 不正确的凭证 */
            code = 2;
        } catch (ExcessiveAttemptsException eae) {
            /* 认证次数超过限制 */
            code = 3;
        } catch (UnknownAccountException uae) {
            /* 未知的账号 */
            /* 账号不存在，但是提示还是用户名或密码错误 */
            code = 4;
        } catch (LockedAccountException lae) {
            /* 账号被锁定 */
            code = 5;
        } catch (Exception e) {
            /* 其他错误 */
            code = 1;
        } finally {
            if (subject.isAuthenticated()) {
                /* 登录成功 */
                code = 0;
            } else {
                /* 登录失败 */
                token.clear();
            }
        }
        return new Response(code, loginMessage[code]);
    }

    /**
     * 执行 退出登录 操作
     *
     * @return 退出登录状态码
     */
    public Response logout() {
        int code = 0;
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new Response(code, logoutMessage[code]);
    }

    /**
     * 检查用户是否已经登录
     *
     * @return 是否登录
     */
    public boolean isAuthenticated() {
        Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated();
    }
}
