
package com.example.cssm.controller;

import com.example.cssm.domain.HostHolder;
import com.example.cssm.domain.Message;
import com.example.cssm.domain.User;
import com.example.cssm.domain.ViewObject;
import com.example.cssm.domain.view.MessageList;
import com.example.cssm.domain.view.MessageView;
import com.example.cssm.service.MessageService;
import com.example.cssm.service.UserService;
import com.example.cssm.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @RequestMapping(value = {"/msg/detail"},method = {RequestMethod.GET})
    public String conversationDetail(Model model,@RequestParam("conversationId") String conversationId){
        try{
            List<MessageView> messages = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationDetail(conversationId,0,10);
            for(Message message : conversationList){
                MessageView messageView = new MessageView();
                messageView.setMessage(message);
                User user = userService.getUser(message.getFromId());
                if(user == null){
                    continue;
                }
                messageView.setUser(user);
                messages.add(messageView);
            }
            model.addAttribute("messages",messages);
            return "letterDetail";
        }catch (Exception e){
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<MessageView> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId,0,10);
            for(Message msg : conversationList){
                MessageView messageVO = new MessageView();
                messageVO.setMessage(msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                messageVO.setUser(user);
                messageVO.setUnReadCount(messageService.getUnreadCount(localUserId,msg.getConversationId()));
                conversations.add(messageVO);
            }
            model.addAttribute("conversations",conversations);
            return "letter";
        }catch (Exception e){
            logger.error("获取站内信列表失败" + e.getMessage());
        }

        return "letter";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        logger.info("正在增加站内信信息");
        Message msg = new Message();
        msg.setContent(content);
        msg.setCreatedDate(new Date());
        msg.setToId(toId);
        msg.setFromId(fromId);
        msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :
                String.format("%d_%d", toId, fromId));
        messageService.addMessage(msg);
        return ToutiaoUtil.getJSONString(msg.getId());
    }

}
