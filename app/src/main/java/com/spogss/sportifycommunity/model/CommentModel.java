package com.spogss.sportifycommunity.model;

import com.spogss.sportifycommunity.data.Comment;
import com.spogss.sportifycommunity.data.User;

/**
 * Created by Pauli on 13.06.2018.
 */

public class CommentModel {
    private Comment comment;
    private User user;

    public CommentModel(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }

    public CommentModel() {
    }

    public Comment getComment() {
        return comment;
    }
    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
