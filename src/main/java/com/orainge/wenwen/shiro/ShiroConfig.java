package com.orainge.wenwen.shiro;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
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
    private final String[] anonUrl = {"/login", "/auth/**", "/static/**"};
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
        map.put("/**", "user"); // 不能写 authc，否则即使是选择了"记住我"，都不能登录
        bean.setFilterChainDefinitionMap(map);
        return bean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager()); // 自定义session管理
        securityManager.setRealm(userRealm());
        securityManager.setRememberMeManager(rememberMeManager()); // 注入 "记住我" 管理器
        return securityManager;
    }

    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager() {
        // 要在上面加入自定义的 session管理 securityManager.setSessionManager(sessionManager());
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false); // 去掉 shiro 登录时 url 里的 JSESSIONID
        // 这里可以不设置。Shiro有默认的session管理。如果缓存为Redis则需改用Redis的管理
        return defaultWebSessionManager;
    }

    @Bean(name = "userRealm")
    public UserRealm userRealm() {
        return new UserRealm();
    }

    /**
     * cookie管理对象;
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("QF5HMyZAWDZYRyFnSGhTdQ=="));
        return cookieRememberMeManager;
    }

    /**
     * cookie对象;
     * rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等。
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // 记住我cookie生效时间7天 单位秒
        simpleCookie.setMaxAge(7 * 24 * 60 * 60);
        return simpleCookie;
    }
}
