package com.talkber.pojo.dto;

import com.talkber.pojo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王浩然
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private String uuid;
    private String email;
    private String password;
    private String nickname;
    private String address;
    private String desc;
}
