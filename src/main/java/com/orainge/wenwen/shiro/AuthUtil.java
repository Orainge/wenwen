package com.orainge.wenwen.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    // 错误提示 状态码对应数组的下标
    private final String[] resultTips = {"登录成功", "用户名或密码不正确“”“”“", "用户名或密码错误次数过多", "账户不存在", "内部错误"};

    public String getResultTips(int resultId) {
        return (resultId >= 0 && resultId < resultTips.length) ? resultTips[resultId] : null;
    }

    public int auth(String principal, String password) {
        return auth(principal, password, null);
    }

    public int auth(String principal, String password, String[] logParameters) {
        int result = -1;
        Subject subject = SecurityUtils.getSubject(); // 从SecurityUtils里边创建一个 subject
        UsernamePasswordToken token = new UsernamePasswordToken(principal, password); // 在认证提交前准备 token（令牌）

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

        // TODO 在日志管理器输出登录结果，具体写入登陆日期、登录时间等等。
//        String resultTip = Authorization.resultTips[result]; // 状态码对应的字符
        return result;
    }


}
