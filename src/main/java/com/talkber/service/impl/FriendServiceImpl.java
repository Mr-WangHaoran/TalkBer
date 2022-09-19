package com.talkber.service.impl;

import com.talkber.dao.FriendDao;
import com.talkber.pojo.dto.FriendDto;
import com.talkber.pojo.model.Friend;
import com.talkber.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 王浩然
 * @description
 */
@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendDao friendDao;

    @Override
    public Friend findAllFriendUUID(String uuid) {
        List<String> allFriendUUID = friendDao.findAllFriendUUID(uuid);
        Friend friend = new Friend();
        friend.setUuid(uuid);
        friend.setFriendsUUID(allFriendUUID);
        return friend;
    }

    @Override
    public FriendDto findFriendByUUID(String uuid) {
        return friendDao.findFriendByUUID(uuid);
    }

    @Override
    public List<FriendDto> findFriendsByUUIDs(List<String> uuids) {
        return friendDao.findFriendsByUUIDs(uuids);
    }

    @Override
    public String findUUIDByEmail(String email) {
        return friendDao.findUUIDByEmail(email);
    }

    @Override
    public Integer insertFriend(String uuid, String friendUUID) {
        return friendDao.insertFriend(uuid, friendUUID);
    }
}
