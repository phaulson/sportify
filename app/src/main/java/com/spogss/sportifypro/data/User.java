package com.spogss.sportifypro.data;

import java.io.Serializable;

/**
 * Created by samue on 27.03.2018.
 */

public class User implements Serializable{
    private static int nextId;
    private int id;
    private String password;
    private String username;
    private String biography;

    public User(String username, String password) {
        this.id = nextId++;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
