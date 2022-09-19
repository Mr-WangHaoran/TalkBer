package com.talkber.pojo.dto;

import lombok.Data;

/**
 * @author 王浩然
 * @description
 */
@Data
public class AddMsgDto {
    private Integer type;
    private String addEmail;
    private String addMsg;
}
