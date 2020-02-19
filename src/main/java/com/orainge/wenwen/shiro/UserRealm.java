package com.orainge.wenwen.shiro;

import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.mybatis.entity.User;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    //@Lazy //？就是这里，必须延时加载，根本原因是bean实例化的顺序上，shiro的bean必须要先实例化，否则@Cacheable注解无效，理论上可以用@Order控制顺序
    private UserMapper userMapper;

    @Override
    // 权限认证，即登录过后，每个身份不一定，对应的所能看的页面也不一样。
    // 由于不使用授权管理，因此返回 null
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String principal = token.getPrincipal().toString();
        String password = new String((char[]) token.getCredentials()); // 传过来的密码已经是加了密的
        User userLoginInfo = userMapper.getUserLoginInfoByPricipal(principal);

        if (userLoginInfo == null) {
            /* 用户不存在 */
            throw new UnknownAccountException();
        }

        /* 用户存在，开始验证 */
        String dbPassword = userLoginInfo.getPassword();
        if (!password.equals(dbPassword)) {
            // 用户名或密码错误
            throw new IncorrectCredentialsException();
        }

        /* 登录成功返回的信息 */
        return new SimpleAuthenticationInfo(principal, password, getName());
    }
}
