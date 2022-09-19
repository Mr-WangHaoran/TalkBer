package com.talkber.pojo.dto;

import com.talkber.pojo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 王浩然
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto extends User implements Serializable {
    private String phoneOrEmail;
    private String password;
    private String rememberMe;
    private String code;
}
