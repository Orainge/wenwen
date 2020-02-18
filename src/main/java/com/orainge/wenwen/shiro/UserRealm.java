package com.orainge.wenwen.shiro;

import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Map;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    @Lazy //？就是这里，必须延时加载，根本原因是bean实例化的顺序上，shiro的bean必须要先实例化，否则@Cacheable注解无效，理论上可以用@Order控制顺序
    private UserMapper userMapper;

    @Override
    // 权限认证，即登录过后，每个身份不一定，对应的所能看的页面也不一样。
    // 由于不使用授权管理，因此返回 null
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("-------身份认证方法--------");
        System.out.println("token.getPrincipal:" + token.getPrincipal());
        System.out.println("token.getCredentials:" + token.getCredentials());

        String userName = token.getPrincipal().toString();
        String password = token.getCredentials().toString();
        List<Map<String, Object>> userLoginInfo = userMapper.getUserLoginInfo(userName);

        // TODO 测试后删除
        System.out.println("获取到的用户信息：" + JSON.toJSONString(userLoginInfo));

        // 开始认证
        if (userLoginInfo == null) {
            // 用户不存在
            throw new UnknownAccountException();
        } else if (!password.equals(userLoginInfo.get(0).get("password"))) {
            // 用户名或密码错误
            throw new IncorrectCredentialsException();
        }
        // TODO 认证次数超过限制的判断还没实现

        // 登录成功返回的信息
        return new SimpleAuthenticationInfo(userName, password, getName());
    }
}
