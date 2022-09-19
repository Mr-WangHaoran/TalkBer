package com.talkber.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 王浩然
 * @description 系统控制跳转
 */
@Controller
public class SysController {

    @RequestMapping({"/retrieve-pwd.html","/retrieve-pwd","/retrieve"})
    public String toRetrievePwd(){
        return "retrieve-pwd";
    }

    @RequestMapping({"/registry.html","/registry"})
    public String toRegistry(){
        return "registry";
    }

    @RequestMapping({"/index","/index.html"})
    public String toIndex(){
        return "index";
    }

    @RequestMapping({"/404.html"})
    public String to404(){ return "404"; }

    @RequestMapping({"/500.html"})
    public String to500(){ return "500"; }
}
