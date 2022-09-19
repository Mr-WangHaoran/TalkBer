package com.talkber.service;

import com.talkber.pojo.dto.UpdateUserDto;
import com.talkber.pojo.dto.UserRegistryDto;
import com.talkber.pojo.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    Integer insertUserToRegistry(UserRegistryDto urd);

    List<UserRegistryDto> findUserByPhone(String phoneNumber);

    List<UserRegistryDto> findUserByEmail(String email);

    User findUserToLogin(String phoneOrEmail,String password);

    Integer updatePwd(String password,String email);

    Integer updatePersonalData(UpdateUserDto user);

    User findUserDataByEmail(String email);

    Integer updateLastLoginTime(String date,String uuid);

    Integer updateStatus(Integer status,String uuid);

    Integer updateUserAvatar(String userAvatar,String uuid);
}
