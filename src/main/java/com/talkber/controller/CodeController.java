package com.talkber.controller;

import com.talkber.util.ImageUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Random;

/**
 * @author 王浩然
 * @description 生成验证码
 */
@Controller
public class CodeController {

    @SneakyThrows
    @RequestMapping("/verifyCode")
    private void getVerifyCode(HttpServletResponse response, HttpSession session){
        Map map = ImageUtil.getVerifyCodeImg();
        BufferedImage buffImg = (BufferedImage) map.get("buffImg");
//        数字验证
        StringBuffer randomCode = (StringBuffer) map.get("randomCode");
        session.setAttribute("randomCode",randomCode);
        // 禁止图像缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        response.setContentType("image/jpeg");
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(buffImg,"jpeg",outputStream);
        outputStream.close();
    }

}
