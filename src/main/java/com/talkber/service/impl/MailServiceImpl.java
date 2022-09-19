package com.talkber.service.impl;

import com.talkber.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author 王浩然
 * @description
 */
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

//    发送人
    @Value("${spring.mail.username}")
    private String form;

    /**
     *  发送简单的邮件，不包含图片
     * @param to 发送给谁
     * @param subject 发送主题
     * @param content 发送内容
     */
    public void sendSimpleMail(String to,String subject,String content){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(this.form);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);

        mailSender.send(simpleMailMessage);
    }
}
