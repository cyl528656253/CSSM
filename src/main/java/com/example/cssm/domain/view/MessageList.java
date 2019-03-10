package com.example.cssm.domain.view;

public class MessageList {
    private String conversationId;
    private int cnt;

    public int getCnt() {
        return cnt;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}

