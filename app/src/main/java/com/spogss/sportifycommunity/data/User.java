package com.spogss.sportifycommunity.data;

import java.io.Serializable;

/**
 * Created by Pauli on 27.03.2018.
 */

public class User implements Serializable {
    private static int nextId;
    private int id;
    private String password;
    private String username;
    private String biography;
    private int profilePic;

    public User(String username, String password, int profilePic) {
        this.profilePic = profilePic;
        this.id = nextId++;
        this.username = username;
        this.password = password;
    }

    public User(String username, int profilePic) {
        this(username, null, profilePic);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }
}
