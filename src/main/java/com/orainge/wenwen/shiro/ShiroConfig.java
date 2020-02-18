package com.orainge.wenwen.shiro;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    /* 用户未登录时跳转到的登陆页面 */
    private final String loginUrl = "/login";
    /* 用户登录后跳转的页面 */
    private final String successUrl = "/index";
    /* anon: 可以匿名访问的 URL */
    private final String[] anonUrl = new String[]{"/static/**"};
    /* authc: 必须认证通过才可以访问的 URL */
    private final String[] authcUrl = new String[]{};

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl(loginUrl);
        bean.setSuccessUrl(successUrl);

        Map<String, String> map = new LinkedHashMap<String, String>();
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
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm());
        return defaultWebSecurityManager;
    }

    @Bean(name = "userRealm")
    public UserRealm userRealm() {
        return new UserRealm();
    }
}
