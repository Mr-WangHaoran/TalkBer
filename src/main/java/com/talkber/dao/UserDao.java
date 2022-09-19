package com.talkber.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.talkber.pojo.dto.UpdateUserDto;
import com.talkber.pojo.dto.UserRegistryDto;
import com.talkber.pojo.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserDao extends BaseMapper<UserRegistryDto> {

    @Select("select * from user where (phone_number = #{phoneOrEmail} AND `password`=#{password})\n" +
            "OR (email = #{phoneOrEmail}  AND `password` = #{password})")
    User findUserToLogin(@Param("phoneOrEmail") String phoneOrEmail,@Param("password") String password);

    @Update("update user set password=#{password} where email = #{email}")
    Integer updatePwd(@Param("password") String password,@Param("email") String email);

    Integer updatePersonalData(UpdateUserDto user);

    @Select("select * from user where email=#{email}")
    User findUserDataByEmail(String email);

//    insert语句不能带有where条件，这里只能用update
    @Update("update user set last_login_time=#{date} where uuid=#{uuid} ")
    Integer updateLastLoginTime(@Param("date") String date,@Param("uuid") String uuid);

//    修改用户的状态，1为在线，2为繁忙，3为离线
    @Update("update user set status=#{status} where uuid=#{uuid}")
    Integer updateStatus(@Param("status") Integer status,@Param("uuid") String uuid);

    @Update("update user set user_avatar=#{userAvatar} where uuid=#{uuid}")
    Integer updateUserAvatar(@Param("userAvatar")String userAvatar,@Param("uuid") String uuid);
}
