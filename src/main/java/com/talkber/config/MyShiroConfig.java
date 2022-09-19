package com.talkber.config;

import com.talkber.realm.UserRealm;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 王浩然
 * @description shiro配置类
 */
@Configuration
public class MyShiroConfig {

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean
            (@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        //关联SecurityManager
        shiroFilter.setSecurityManager(defaultWebSecurityManager);

        Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/index","user");
        filterMap.put("/index.html","user");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        shiroFilter.setLoginUrl("/login");
        return shiroFilter;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberCookie());
//        cookie加密
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }


    @Bean
    public SimpleCookie rememberCookie(){
//        cookie的名字
        SimpleCookie rememberMe = new SimpleCookie("rememberMe");
//        cookie不设置过期时间，默认是会话性cookie，用户关闭浏览器即失效
//        rememberMe.setMaxAge(60*60*24*2);
        return rememberMe;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
//        注入记住我管理器
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean("userRealm")
    public UserRealm getUserRealm(){
        return new UserRealm();
    }
}
