package com.talkber.pojo.dto;

import lombok.Data;

/**
 * @author 王浩然
 * @description 消息实体类
 */
@Data
public class MessageDto {
//    信息来源uuid
    private String fromUUID;
//    信息内容
    private String message;
    private String userAvatar;
    private String nickname;
    private Integer status;
}
