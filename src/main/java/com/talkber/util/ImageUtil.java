package com.talkber.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author 王浩然
 * @description
 */
public class ImageUtil {

    public static Map getVerifyCodeImg() {
        int width = 60;
        int height = 20;
        // 创建具有可访问图像数据缓冲区的Image
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();

        // 创建一个随机数生成器
        Random random = new Random();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 创建字体，字体的大小应该根据图片的高度来定
        Font font = new Font("Times New Roman", Font.PLAIN, 18);
        // 设置字体
        g.setFont(font);

        // 画边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);

        // 随机产生160条干扰线
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 160; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            g.drawLine(x, y, x + x1, y + y1);
        }

        // randomCode 用于保存随机产生的验证码
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;

        // 随机产生4位数字的验证码
        for (int i = 0; i < 4; i++) {
            // 得到随机产生的验证码数字
            String strRand = String.valueOf(random.nextInt(10));

            // 产生随机的颜色分量来构造颜色值
            red = random.nextInt(110);
            green = random.nextInt(50);
            blue = random.nextInt(50);

            // 用随机产生的颜色将验证码绘制到图像中
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, 13 * i + 6, 16);

            randomCode.append(strRand);
        }
        Map map = new HashMap();
        map.put("randomCode",randomCode);
        map.put("buffImg",buffImg);
        return map;
    }
}
