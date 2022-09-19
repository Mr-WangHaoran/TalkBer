package com.talkber.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.talkber.pojo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 王浩然
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class UserRegistryDto extends User {
    private String uuid;
    private String nickname;
    @TableField("phone_number")
    private String phoneNumber;
    private String email;
    private Integer sex;
    private String password;
    @TableField("last_login_time")
    private Date lastLoginTime; //上次登录时间
    @TableField("register_time")
    private Date registerTime;  //注册时间
}
