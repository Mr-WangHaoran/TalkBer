package com.talkber.ws;

import com.alibaba.fastjson.JSON;
import com.talkber.config.MyWebsocketConfig;
import com.talkber.pojo.dto.AddMsgDto;
import com.talkber.pojo.dto.MessageDto;
import com.talkber.pojo.model.User;
import com.talkber.service.FriendService;
import com.talkber.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.context.SpringContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王浩然
 * @description 大坑：在websocket中注入bean但为null的原因，但
 * 是客户端每建立一个链接就会创建一个对象，这个对象没有任何的bean注入操作，所以注入只能注入静态的bean
 */
@Component
@ServerEndpoint(value = "/chat",configurator = MyWebsocketConfig.class)
public class ChatEndPoint {

    final static Logger logger = LoggerFactory.getLogger(ChatEndPoint.class);

    private static ListOperations listOperations;

    private static RedisTemplate redisTemplate;

    private static FriendService friendService;

    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        ChatEndPoint.userService = userService;
    }

    @Autowired
    public void setFriendService(FriendService friendService) {
        ChatEndPoint.friendService = friendService;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        ChatEndPoint.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setListOperations(ListOperations listOperations){
        ChatEndPoint.listOperations = listOperations;
    }


    private static Map<String,ChatEndPoint> clients = new ConcurrentHashMap<>();
//    此session非彼session，这里是客户端和服务端建立连接后客户端的session，每个客户端有唯一的一个session
    private Session session;
    private HttpSession httpSession;
    private String uuid;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.session = session;
        this.httpSession = httpSession;
        User user = (User) httpSession.getAttribute("user");
        this.uuid = user.getUuid();
        clients.put(user.getUuid(),this);
        logger.info("建立了一个新的websocket连接，现有连接数："+clients.size());
//        判断redis中是否有发往自己的消息，如果有则进行消费
        if(redisTemplate.hasKey(this.uuid)){
//            添加一条无效信息
//            listOperations.leftPush(uuid,new MessageDto());
            for (int i = 0; i <=listOperations.size(this.uuid); i++) {
                MessageDto messageDto = (MessageDto) listOperations.rightPop(this.uuid);
                session.getAsyncRemote().sendText(JSON.toJSONString(messageDto));
            }
            MessageDto messageDto1 = (MessageDto) listOperations.rightPop(this.uuid);
            session.getAsyncRemote().sendText(JSON.toJSONString(messageDto1));
            logger.info("发现消息队列中有发往"+uuid+"的消息，消息还剩下"+listOperations.size(this.uuid)+"条,已经消费完毕");
//            发送完所有消息后删除此key
            redisTemplate.delete(this.uuid);
        }

    }

    @OnMessage
    public void onMessage(String message,Session session){
//        说明发送过来的是请求好友信息
        if(message.indexOf("{")!=-1){
            AddMsgDto addMsgDto = JSON.parseObject(message, AddMsgDto.class);
//            发送好友申请
            if (addMsgDto.getType()==0){
                addFriend(addMsgDto);
            }else if(addMsgDto.getType()==2){
//                好友申请结果
                String addEmail = addMsgDto.getAddEmail();
                String fromEmail = addEmail.substring(addEmail.indexOf(",")+1);
                String toEmail = addEmail.substring(0,addEmail.indexOf(","));
                String uuid1 = friendService.findUUIDByEmail(fromEmail);
                String uuid2 = friendService.findUUIDByEmail(toEmail);
                ChatEndPoint chatEndPoint = clients.get(uuid1);
                String addMsg = addMsgDto.getAddMsg();
                addMsgDto.setAddEmail(fromEmail);
                if(addMsg.equals("同意了您的好友申请")){
                    Integer i1 = friendService.insertFriend(uuid1, uuid2);
                    Integer i2 = friendService.insertFriend(uuid2, uuid1);
                    logger.info("插入两条好友信息：i1="+i1+",i2="+i2);
                }
                if(chatEndPoint!=null){
                    //说明用户在线
                    chatEndPoint.session.getAsyncRemote().sendText(JSON.toJSONString(addMsgDto));
                }else{
                    //不在线
                }
            }
            return;
        }
        String fuuid = (String) httpSession.getAttribute("fuuid");
        logger.info("收到"+uuid+"的信息："+message+"\n要发往:"+fuuid);
        ChatEndPoint chatEndPoint = clients.get(fuuid);
        logger.info("要发送的对象"+(chatEndPoint==null?"不在线":"在线"));
//        创建消息对象
        User user = (User) httpSession.getAttribute("user");
        MessageDto messageDto = new MessageDto();
        messageDto.setFromUUID(uuid);
        messageDto.setMessage(message);
        messageDto.setUserAvatar(user.getUserAvatar());
        messageDto.setNickname(user.getNickname());
        messageDto.setStatus(user.getStatus());
//        说明发送对象存在
        if (chatEndPoint!=null){
            chatEndPoint.session.getAsyncRemote().sendText(JSON.toJSONString(messageDto));
        }else{
//            如果发送对象不存在，将信息存储到redis中
            Long i = listOperations.leftPush(fuuid, messageDto);
            logger.info("消息队列注入（i）:i="+i);

        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        clients.remove(uuid);
        logger.info("断开了一个websocket连接，现有连接数："+clients.size());
        session.close();
    }

    public void addFriend(AddMsgDto addMsgDto){
        User user = (User) httpSession.getAttribute("user");
        String vmsg = addMsgDto.getAddMsg();
        String uuid = friendService.findUUIDByEmail(addMsgDto.getAddEmail());
        ChatEndPoint chatEndPoint = clients.get(uuid);
//        修改addMsgDto,type改为1，1为好友请求，0为请求好友，2为同意请求
        addMsgDto.setType(1);
        addMsgDto.setAddEmail(user.getEmail());
        addMsgDto.setAddMsg("邮箱为：<span style='color:blue;'>"+user.getEmail()+"</span>要添加您为好友，验证消息为：" +
                "【<span style='color:red;'>"+vmsg+"</span>】，是否同意？");
        if(chatEndPoint!=null){
//            说明要添加好友的用户在线
            chatEndPoint.session.getAsyncRemote().sendText(JSON.toJSONString(addMsgDto));
        }else{
//            说明要添加好友的用户不在线
        }
    }
}
