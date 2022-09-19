package com.talkber.controller;

import com.alibaba.fastjson.JSON;
import com.talkber.constant.ResultConstant;
import com.talkber.pojo.dto.*;
import com.talkber.pojo.model.User;
import com.talkber.service.FriendService;
import com.talkber.service.MailService;
import com.talkber.service.UserService;
import com.talkber.util.DateFormat;
import com.talkber.util.UUIDUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author 王浩然
 * @description
 */
@Controller
public class UserController {

    static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private MailService mailService;

    @Value("${spring.mail.username}")
    private String mailSender;

    /*
    以前的用户登录，判断是否点击了记住我
     */
    @RequestMapping("/last-user-login")
    @ResponseBody
    public String lastUserLogin(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Cookie[] cookies = request.getCookies();
//        如果cookie不为空，说明上次用户点击了记住我，那么跳过验证码验证阶段
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("rememberMe")){
                    return "index";
                }
            }
        }
       return "login";
    }

    @RequestMapping("/user-login")
    @ResponseBody
    public String userLogin(UserLoginDto uld, HttpSession session, HttpServletRequest request
            , HttpServletResponse response){
        ResultDto resultDto = new ResultDto();

        StringBuffer randomCode = (StringBuffer) session.getAttribute("randomCode");
        if(!randomCode.toString().equals(uld.getCode())){
            resultDto.setStatus("error");
            resultDto.setMessage("验证码不正确");
            return JSON.toJSONString(resultDto);
        }
        Subject subject = SecurityUtils.getSubject();
//        加密盐值为TalkBerIsSoCool
        Md5Hash password = new Md5Hash(uld.getPassword(),"TalkberIsSoCool",3);
        UsernamePasswordToken token = new UsernamePasswordToken(uld.getPhoneOrEmail(),password.toString());
        token.setRememberMe(Boolean.valueOf(uld.getRememberMe()));
        try{
//            这里的login将会把token交付给securityManager，sm再去UserRealm中认证用户
            subject.login(token);
//            登录成功跳转
            resultDto.setStatus("success");resultDto.setMessage("允许登录");
            return JSON.toJSONString(resultDto);
        }catch (Exception e){
            //用户名不存在
            resultDto.setStatus("error");
            resultDto.setMessage("用户不存在");
            return JSON.toJSONString(resultDto);
        }finally {
//            如果用户认证成功，存储一些关键信息
            if(subject.isAuthenticated()){
//                获得此用户的基本信息
                User user = userService.findUserToLogin(uld.getPhoneOrEmail(),password.toString());
//                修改用户的状态，判断用户状态是否为离线，如果为繁忙就不修改
                if (user.getStatus()==3){
                    Integer row = userService.updateStatus(1, user.getUuid());
                    logger.info("登录将用户状态由离线修改为在线");
                }
//                获取此用户的所有好友
                List<FriendDto> friends = friendService.findFriendsByUUIDs(friendService.findAllFriendUUID(user.getUuid()).getFriendsUUID());
//                将此用户的上次登录时间存储到数据库中
                Date date = new Date();
                String time = DateFormat.formatDate(date, "yyyy-MM-dd HH:mm:ss");
                Integer i = userService.updateLastLoginTime(time,user.getUuid());
                logger.info("插入上次登录时间(i>0成功)，i="+i+"；登录时间为："+time);
                user.setLastLoginTime(date);
                user.setStatus(1);
                session.setAttribute("user",user);
                session.setAttribute("friends",friends);
            }
        }
    }


    /*
    用户注册
     */
    @RequestMapping("/user-registry")
    @ResponseBody
    public String insertUserToRegistry(UserRegistryDto urd){
        ResultDto resultDto = new ResultDto();

        Integer i = userService.insertUserToRegistry(urd);
        logger.info("插入新用户状态（i>0）成功：i="+i);
        if(i>0){
            resultDto.setStatus(ResultConstant.success.SUCCESS);
            resultDto.setMessage(ResultConstant.success.Message);
        }else{
            resultDto.setStatus(ResultConstant.success.SUCCESS);
            resultDto.setMessage(ResultConstant.success.Message);
        }
        return JSON.toJSONString(resultDto);
    }

    /*
    查询手机号
     */
    @RequestMapping("/searchPhone")
    @ResponseBody
    public String searchPhone(String phoneNumber){
        List<UserRegistryDto> users = userService.findUserByPhone(phoneNumber);
        if(users.size()>0){
            return JSON.toJSONString(new ResultDto("error","该手机号已被注册"));
        }else{
            return JSON.toJSONString(new ResultDto("success","该手机号可以被注册"));
        }
    }

    /*
    查询邮箱
     */
    @RequestMapping("/searchEmail")
    @ResponseBody
    public String searchEmail(String email){
        List<UserRegistryDto> users = userService.findUserByEmail(email);
        if(users.size()>0){
            return JSON.toJSONString(new ResultDto("error","该邮箱已被注册"));
        }else{
            return JSON.toJSONString(new ResultDto("success","该邮箱可以被注册"));
        }
    }

    /*
    发送修改密码的验证码邮箱
     */
    @RequestMapping("/sendCodeMail")
    @ResponseBody
    public String sendCodeMail(String email,HttpSession session){
        String randCode = UUIDUtil.getRandCode();
        session.setAttribute("randCode",randCode);
        mailService.sendSimpleMail(email,"找回密码验证码","验证码为："+randCode+"，请不要泄露给他人，以保证您的账号安全！");
        return "ok";
    }

    /*
    验证验证码是否正确
     */
    @RequestMapping("/mailCodeVerify")
    @ResponseBody
    public String mailCodeVerify(String code,HttpSession session){
        ResultDto resultDto = new ResultDto();
        String randCode = (String) session.getAttribute("randCode");
        if(code.equals(randCode)){
            resultDto.setMessage("验证码正确");
            resultDto.setStatus("success");
            return JSON.toJSONString(resultDto);
        }else{
            resultDto.setMessage("验证码错误");
            resultDto.setStatus("error");
            return JSON.toJSONString(resultDto);
        }
    }

    /*
    修改密码
     */
    @RequestMapping("/update-pwd")
    @ResponseBody
    public String updatePwd(@RequestBody UpdateUserDto userDto){
        ResultDto resultDto = new ResultDto();
        Integer i = userService.updatePwd(userDto.getPassword(),userDto.getEmail());
        if(i>0){
            resultDto.setStatus(ResultConstant.success.SUCCESS);
            resultDto.setMessage("密码修改成功");
        }else{
            resultDto.setStatus(ResultConstant.error.ERROR);
            resultDto.setMessage("密码修改失败");
        }
        return JSON.toJSONString(resultDto);
    }
    /*
    个人主页修改个人信息
     */
    @RequestMapping("/update-my-data")
    @ResponseBody
    public String updatePersonalData(UpdateUserDto user,HttpSession session){
        ResultDto resultDto = new ResultDto();
        Integer i = userService.updatePersonalData(user);
        User user1 = userService.findUserDataByEmail(user.getEmail());
        session.setAttribute("user",user1);
        if(i>0){
            resultDto.setStatus(ResultConstant.success.SUCCESS);
            resultDto.setMessage("修改成功！");
        }else{
            resultDto.setStatus(ResultConstant.error.ERROR);
            resultDto.setMessage("修改失败！");
        }
        return JSON.toJSONString(resultDto);
    }

    @RequestMapping("/updateStatus")
    public String updateStatus(Integer status,String uuid,HttpSession session){
        User user = (User) session.getAttribute("user");
        Integer i = userService.updateStatus(status,uuid);
        user.setStatus(status);
        session.setAttribute("user",user);
        logger.info("修改用户状态"+(i>0?"成功！":"失败！"));
        return "index";

    }

    /*
    用户退出，采用默认的shiro配置，并额外添加一些配置
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user!=null){
            Subject subject = SecurityUtils.getSubject();
            if(user.getStatus()!=3){
                Integer i = userService.updateStatus(3, user.getUuid());
                logger.info("用户退出，将在线改为离线状态，修改状态:"+i);
            }
            subject.logout();
        }
        return "login";
    }
}
