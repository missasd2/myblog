package com.example.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken; // 判断token是否是我们自己定义的JwtToken； 如果是的话说明支持的是我们自定义的token
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 返回权限
        return null;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 传入token进行验证
        JwtToken jwtToken = (JwtToken) token;

        /**
         * 返回的是Claims
         *     String ISSUER = "iss";
         *     String SUBJECT = "sub";
         *     String AUDIENCE = "aud";
         *     String EXPIRATION = "exp";
         *     String NOT_BEFORE = "nbf";
         *     String ISSUED_AT = "iat";
         *     String ID = "jti";
         *
         *     1. 获取userId
         *     2. 根据id查出用户
         *     3. 在用户可用的情况下，把用户的非敏感基本信息封装到profile中，并返回给shiro
         */
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();

        User user = userService.getById(Long.valueOf(userId));

        if (user == null){
            throw  new UnknownAccountException("账户不存在");
        }

        if (user.getStatus() == -1){
            throw  new LockedAccountException("账户被锁定");
        }

        System.out.println("--------------");

        AccountProfile profile = new AccountProfile();
        // 将user的属性 转移到 profile里面
        BeanUtil.copyProperties(user, profile);

        // public SimpleAuthenticationInfo(Object principal, Object credentials, String realmName)
        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
    }
}
