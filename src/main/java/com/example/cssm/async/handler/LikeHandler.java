package com.example.cssm.async.handler;

import com.example.cssm.async.EventHandler;
import com.example.cssm.async.EventModel;
import com.example.cssm.async.EventType;
import com.example.cssm.domain.Message;
import com.example.cssm.domain.User;
import com.example.cssm.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        message.setFromId(3);
        User user = new User();
        message.setContent("用户 " + user.getName() +
                "赞了你的咨询，http：//127.0.0.1"+ model.getEntityId());
        message.setCreatedDate(new Date());
        message.setConversationId("3_11");
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
