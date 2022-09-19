package com.talkber.pojo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 王浩然
 * @description 用户类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
/*
shiro要实现记住我功能，必须将管理的model对象实现可序列化（implements Serializable）
 */
public class User implements Serializable {
    @TableId(value="user_id",type = IdType.AUTO)
    private long userId;
    private String uuid;   //注册时间+uuid
    private String nickname;
    @TableField("phone_number")
    private String phoneNumber;
    private String email;
    private Integer sex;
    private String address;
    @TableField("user_avatar")
    private String userAvatar;
    private String password;
    @TableField("`desc`")
    private String desc;
    private Integer status;     //用户状态【3-离线，2-繁忙，1-在线】
    @TableField("last_login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date lastLoginTime; //上次登录时间
    @TableField("register_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date registerTime;  //注册时间
}
