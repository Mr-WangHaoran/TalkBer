package com.talkber.dao;

import com.talkber.pojo.dto.FriendDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface FriendDao {

    List<String> findAllFriendUUID(String uuid);

    List<FriendDto> findFriendsByUUIDs(List<String> uuids);

    @Select("select `uuid`,nickname,email,address,user_avatar,`desc`,status,last_login_time from user where uuid=#{uuid} ")
    FriendDto findFriendByUUID(String uuid);

    @Select("select uuid from user where email=#{email}")
    String findUUIDByEmail(String email);

    @Insert("insert into friend(uuid,friend_uuid) values(#{uuid},#{friendUUID}) ")
    Integer insertFriend(@Param("uuid") String uuid,@Param("friendUUID") String friendUUID);
}
