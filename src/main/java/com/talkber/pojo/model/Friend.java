package com.talkber.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 王浩然
 * @description 好友类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend {

    private String uuid;//本人id
    private List<String> friendsUUID;//所有好友的uuid
}
