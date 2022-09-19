package com.talkber.pojo.dto;

import com.talkber.constant.ResultConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王浩然
 * @description 返回给前端的结果类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto {
    private String status; //结果状态 success,error
    private String message;//结果信息
}
