package com.spogss.sportifycommunity.model;

import com.spogss.sportifycommunity.data.Post;
import com.spogss.sportifycommunity.data.User;

import java.io.Serializable;

/**
 * Created by Pauli on 17.04.2018.
 */

public class PostModel implements Serializable {
    private Post post;
    private User user;
    private int numberOfLikes;
    private boolean liked;

    public PostModel(Post post, User user, int numberOfLikes, boolean liked) {
        this.post = post;
        this.user = user;
        this.numberOfLikes = numberOfLikes;
        this.liked = liked;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
