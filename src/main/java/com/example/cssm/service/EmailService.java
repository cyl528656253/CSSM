package com.example.cssm.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class EmailService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private JavaMailSenderImpl javaMailSender;

    public void sendSimpleMail(String sendTo, String title, String content) {

        //创建邮件内容
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("528656253@qq.com");
        message.setTo(sendTo);
        message.setSubject(title);
        message.setText(content);
        //发送邮件
        javaMailSender.send(message);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.qq.com");
        javaMailSender.setPort(465);
        javaMailSender.setUsername("528656253@qq.com");
        javaMailSender.setPassword("ciyxkyzfwynhbhhh");
        Properties properties = new Properties();
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        javaMailSender.setJavaMailProperties(properties);
    }
}
