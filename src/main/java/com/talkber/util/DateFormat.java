package com.talkber.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 王浩然
 * @description
 */
public class DateFormat {

    private static SimpleDateFormat sdf = null;

    public static String formatDate(Date date,String pattern){
        sdf = new SimpleDateFormat(pattern);
        String formatDate = sdf.format(date);
        return formatDate;
    }
}
