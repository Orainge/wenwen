package com.orainge.wenwen.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    /* 用户未登录时跳转到的登陆页面 */
    private final String loginUrl = "/login";
    /* anon: 可以匿名访问的 URL */
    private final String[] anonUrl = new String[]{"/favicon.ico", "/static/**", "/login", "/apiSendActive", "/sendResetPassword", "/auth/**"};
    /* authc: 必须认证通过才可以访问的 URL */
    private final String[] authcUrl = new String[]{};

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl(loginUrl);

        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("/favicon.ico", "anon");
        for (String url : anonUrl) {
            map.put(url, "anon");
        }
        for (String url : authcUrl) {
            map.put(url, "authc");
        }

        // 下面这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
        map.put("/**", "authc");
        bean.setFilterChainDefinitionMap(map);
        return bean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager()); // 自定义session管理
        securityManager.setRealm(userRealm());
        return securityManager;
    }

    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager() {
        // 要在上面加入自定义的 session管理 securityManager.setSessionManager(sessionManager());
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false); // 去掉 shiro 登录时 url 里的 JSESSIONID
        //这里可以不设置。Shiro有默认的session管理。如果缓存为Redis则需改用Redis的管理
        // shiroSessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
        return defaultWebSessionManager;
    }

    @Bean(name = "userRealm")
    public UserRealm userRealm() {
        return new UserRealm();
    }
}
