package com.talkber.controller;

import com.alibaba.fastjson.JSON;
import com.talkber.pojo.model.User;
import com.talkber.service.UserService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 王浩然
 * @description
 */
@RestController
public class UploadController {

    static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/uploadImg")
    public String uploadImg(MultipartFile file, HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user==null)return null;
        logger.info("后端接收到的文件为:"+file.getOriginalFilename());
        logger.info("文件类型："+file.getContentType());
//        收到的文件大小
        logger.info("文件大小："+file.getSize());
//        如果文件大小大于3MB
        if (file.getSize()>2048000){
            Map map = new HashMap();
            map.put("errorMsg","文件大小请不要大于2MB");
            return JSON.toJSONString(map);
        }
//        文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (file.isEmpty())return null;
//        服务器存储图片地址，绝对地址
        String path="E:/upload/";
//        上传时间,时间戳
        long date = new Date().getTime();
//        文件名：时间戳+文件本身的名字+后缀
        String filename = String.valueOf(date)+suffix;
//        构建文件对象
        File dest = new File(path+filename);
//        判断文件夹是否存在
        if (!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }
//        传递
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            logger.info("上传失败，失败原因："+e.getMessage());
            return null;
        }
        Map map = new HashMap<>();
        String url = "http://localhost:8081/img_resource_path/"+filename;
        logger.info("图片已经成功上传至："+url);
        map.put("imgUrl",url);
        Integer i = userService.updateUserAvatar(url, user.getUuid());
        logger.info(user.getNickname()+"用户头像已被修改为："+url);
        user.setUserAvatar(url);
        session.setAttribute("user",user);
        return JSON.toJSONString(map);
    }
}
