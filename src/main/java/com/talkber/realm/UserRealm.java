package com.talkber.realm;

import com.talkber.pojo.model.User;
import com.talkber.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author 王浩然
 * @description
 */
public class UserRealm extends AuthorizingRealm {

    final static Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private UserService userService;

//    授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

//    认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String phoneOrEmail = token.getUsername();
        String password = String.valueOf(token.getPassword());
        logger.info("用户登录，登录账号为："+phoneOrEmail+",登录密码为："+password);
        User user = userService.findUserToLogin(phoneOrEmail, password);
        logger.info("此用户"+(user!=null?"存在":"不存在"));
        if (user==null) {
//            如果数据库中没有这个数据，返回一个异常
            return null;
        }

//        用户名的判断我们自己做，而密码的判断需要交给shiro做,并且判断时shiro会自动调取token中的password
        return new SimpleAuthenticationInfo(user.getEmail(),user.getPassword(),getName());
    }
}
