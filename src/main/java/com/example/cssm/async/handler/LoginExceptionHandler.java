package com.example.cssm.async.handler;

import com.example.cssm.async.EventHandler;
import com.example.cssm.async.EventModel;
import com.example.cssm.async.EventType;
import com.example.cssm.domain.Message;
import com.example.cssm.service.EmailService;
import com.example.cssm.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private EmailService emailService;

    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        int toId = model.getActorId();
        message.setToId(model.getActorId());
        message.setContent("你上次的登录IP异常");
        // SYSTEM ACCOUNT
        message.setFromId(3);
        message.setCreatedDate(new Date());
        message.setConversationId(3 < toId ? String.format("%d_%d",3,toId) :
                String.format("%d_%d",toId,3));
        messageService.addMessage(message);
        System.out.println("你上次的登录IP异常");

        // emailService.sendSimpleMail("534634799@qq.com","头条","你上次的登录IP异常");

        emailService.sendSimpleMail("528656253@qq.com","登陆出错","您好，你的nowcoder头条项目出错！");
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}

