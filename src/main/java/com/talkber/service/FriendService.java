package com.talkber.service;

import com.talkber.pojo.dto.FriendDto;
import com.talkber.pojo.model.Friend;

import java.util.List;

public interface FriendService {

    Friend findAllFriendUUID(String uuid);

    FriendDto findFriendByUUID(String uuid);

    List<FriendDto> findFriendsByUUIDs(List<String> uuids);

    String findUUIDByEmail(String email);

    Integer insertFriend(String uuid,String friendUUID);
}
