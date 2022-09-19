package com.talkber.controller;

import com.alibaba.fastjson.JSON;
import com.talkber.pojo.dto.FriendDto;
import com.talkber.pojo.model.Friend;
import com.talkber.service.FriendService;
import com.talkber.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author 王浩然
 * @description
 */
@Controller
public class FriendController {

    final static Logger logger = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private FriendService friendService;


    @RequestMapping("/getAllFriend")
    @ResponseBody
    public String getAllFriend(String uuid, HttpSession session){
        Friend friend = friendService.findAllFriendUUID(uuid);
        logger.info("查找uid为："+uuid+"的好友"+(friend!=null?"成功":"失败"));
        List<FriendDto> friends = friendService.findFriendsByUUIDs(friend.getFriendsUUID());
//        friends.forEach(System.out::println);
        if(friend!=null && friends!=null){
            session.setAttribute("friends",friends);
        }
        return JSON.toJSONString(friends);
    }

    @RequestMapping("/findFriendByUUID")
    @ResponseBody
    public String findFriendByUUID(String uuid){
        FriendDto friend = friendService.findFriendByUUID(uuid);
        logger.info("查找uuid="+uuid+"的好友信息，friend="+friend);
        return JSON.toJSONString(friend);
    }

    @RequestMapping("/getCurrentTalkingUser")
    @ResponseBody
    public String getCurrentTalkingUser(String uuid,HttpSession session){
        session.setAttribute("fuuid",uuid);
        logger.info("当前通信id："+uuid);
        return "ok";
    }

    @RequestMapping("/findUUIDByEmail")
    @ResponseBody
    public String findUUIDByEmail(String email){
        String uuid = friendService.findUUIDByEmail(email);
        logger.info("email="+email+",uuid="+uuid);
        if (uuid!=null){
            return "ok";
        }else{
            return "no";
        }
    }
}
