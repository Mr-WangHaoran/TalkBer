package com.talkber.service.impl;

import com.talkber.dao.UserDao;
import com.talkber.pojo.dto.UpdateUserDto;
import com.talkber.pojo.dto.UserRegistryDto;
import com.talkber.pojo.model.User;
import com.talkber.service.UserService;
import com.talkber.util.UUIDUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王浩然
 * @description
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Integer insertUserToRegistry(UserRegistryDto urd) {
        Md5Hash jiamiPassword = new Md5Hash(urd.getPassword(),"TalkberIsSoCool",3);
        urd.setUuid(UUIDUtil.backUUID());
        urd.setRegisterTime(new Date());
        urd.setPassword(jiamiPassword.toString());
        return userDao.insert(urd);
    }

    @Override
    public List<UserRegistryDto> findUserByPhone(String phoneNumber) {
        Map map = new HashMap();
        map.put("phone_number",phoneNumber);
        return userDao.selectByMap(map);
    }

    @Override
    public List<UserRegistryDto> findUserByEmail(String email) {
        Map map = new HashMap();
        map.put("email",email);
        return userDao.selectByMap(map);
    }

    @Override
    public User findUserToLogin(String phoneOrEmail, String password) {
        return userDao.findUserToLogin(phoneOrEmail, password);
    }

    @Override
    public Integer updatePwd(String password,String email) {
        Md5Hash jiamiPassword = new Md5Hash(password,"TalkberIsSoCool",3);
        return userDao.updatePwd(jiamiPassword.toString(),email);
    }

    @Override
    public Integer updatePersonalData(UpdateUserDto user) {
        return userDao.updatePersonalData(user);
    }

    @Override
    public User findUserDataByEmail(String email) {
        return userDao.findUserDataByEmail(email);
    }

    public Integer updateLastLoginTime(String date,String uuid){
        return userDao.updateLastLoginTime(date,uuid);
    }

    public Integer updateStatus(Integer status,String uuid){
        return userDao.updateStatus(status,uuid);
    }

    @Override
    public Integer updateUserAvatar(String userAvatar, String uuid) {
        return userDao.updateUserAvatar(userAvatar, uuid);
    }

}
