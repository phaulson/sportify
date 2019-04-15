package com.spogss.sportifycommunity.model;

import com.spogss.sportifycommunity.data.User;

/**
 * Created by Pauli on 10.06.2018.
 */

public class UserModel {
    private User user;
    private boolean following;

    public UserModel(User user, boolean following) {
        this.user = user;
        this.following = following;
    }

    public UserModel() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }
}
