package com.talkber.util;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author 王浩然
 * @description
 */
public class UUIDUtil {

    public static String backUUID(){
        Date date = new Date();
        String now = String.valueOf(date.getTime());
        UUID uuid = UUID.randomUUID();
        return now+uuid;
    }

    public static String getRandCode(){
        String letter = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random random = new Random();
        StringBuffer code = new StringBuffer("");
//        随机产生一个20位的随机码
        for(int i=0;i<20;i++){
            int i1 = random.nextInt(letter.length()-1);
            code.append(letter.charAt(i1));
        }
        return code.toString();
    }
}
