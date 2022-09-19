package com.talkber;

import com.talkber.pojo.dto.FriendDto;
import com.talkber.pojo.dto.MessageDto;
import com.talkber.pojo.dto.UserRegistryDto;
import com.talkber.pojo.model.Friend;
import com.talkber.service.FriendService;
import com.talkber.service.MailService;
import com.talkber.service.UserService;
import com.talkber.util.UUIDUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
class TalkberApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private ValueOperations<String,Object> valueOperations;
    @Test
    void contextLoads() {
        List<FriendDto> friendsByUUIDs = friendService.findFriendsByUUIDs(new ArrayList<>());
        System.out.println(friendsByUUIDs.size());
    }

}
