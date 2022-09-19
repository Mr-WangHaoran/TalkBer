package com.talkber.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class FriendDto {

    private String uuid;
    private String nickname;
    private String email;
    private String address;
    @TableField("user_avatar")
    private String userAvatar;
    private String desc;
    private Integer status;
    @TableField("last_login_time")
    private String lastLoginTime;
}
