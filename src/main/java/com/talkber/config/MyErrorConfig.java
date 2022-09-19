package com.talkber.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @author 王浩然
 * @description
 */
@Configuration
public class MyErrorConfig implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
//        错误类型404
        ErrorPage error_404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
//        错误类型500
        ErrorPage errorPage_500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
        registry.addErrorPages(error_404,errorPage_500);
    }
}
