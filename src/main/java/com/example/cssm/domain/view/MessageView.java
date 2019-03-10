package com.example.cssm.domain.view;

import com.example.cssm.domain.Message;
import com.example.cssm.domain.User;

public class MessageView {

    private Message message;
    private User user;
    private int unReadCount;


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }
}
