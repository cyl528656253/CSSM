package com.example.cssm.domain.view;


import com.example.cssm.domain.Comment;
import com.example.cssm.domain.User;

public class CommentView {
    private User user;
    private Comment comment;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
