package com.orainge.wenwen.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    // 错误提示 状态码对应数组的下标
    private String[] loginMessage = {"登录成功", "账户或密码错误", "用户名或密码错误次数过多", "账户不存在", "内部错误"};
    private String[] logoutMessage = {"退出登录成功", "内部错误"};

    /**
     * 根据登录返回的 code 获取相应的提示
     *
     * @param loginResultCode 登录返回的状态码
     * @return 相应的提示
     */
    public String getLoginMessage(int loginResultCode) {
        return (loginResultCode >= 0 && loginResultCode < loginMessage.length) ? loginMessage[loginResultCode] : null;
    }

    /**
     * 根据退出登录返回的 code 获取相应的提示
     *
     * @param logoutResultCode 退出登录返回的状态码
     * @return 相应的提示
     */
    public String getLogoutMessage(int logoutResultCode) {
        return (logoutResultCode >= 0 && logoutResultCode < logoutMessage.length) ? logoutMessage[logoutResultCode] : null;
    }

    /**
     * 执行 登录 操作
     *
     * @param principal 用户名或邮箱(两者可以同时传入)
     * @param password  密码
     * @return 登录状态码
     */
    public int login(String principal, String password, boolean rememberMe) {
        int result = -1;
        Subject subject = SecurityUtils.getSubject(); // 从SecurityUtils里边创建一个 subject
        // TODO 踢出已登录的用户
        UsernamePasswordToken token = new UsernamePasswordToken(principal, password, rememberMe); // 在认证提交前准备 token（令牌）

        /* 执行认证登陆 */
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException ice) {
            /* 不正确的凭证 */
            result = 1;
        } catch (ExcessiveAttemptsException eae) {
            /* 认证次数超过限制 */
            result = 2;
        } catch (UnknownAccountException uae) {
            /* 未知的账号 */
            result = 3;
        } catch (Exception e) {
            /* 其他错误 */
            result = 4;
        } finally {
            if (subject.isAuthenticated()) {
                /* 登录成功 */
                result = 0;
            } else {
                /* 登录失败 */
                token.clear();
            }
        }
        return result;
    }

    /**
     * 执行 退出登录 操作
     *
     * @return 退出登录状态码
     */
    public int logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        int status = 0;
        return status;
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
