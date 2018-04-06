/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.TreeSet;

/**
 *
 * @author Martin
 */
public class User {
    private int idUser;
    private String username;
    private String password;
    private String biographie;
    private boolean isPro;
    private TreeSet<Plan> plans = new TreeSet<Plan>();
    private TreeSet<Plan> subPlans = new TreeSet<Plan>();
    private TreeSet<Integer> followedUser = new TreeSet<Integer>();
    private TreeSet<Post> posts = new TreeSet<Post>();

    public User(int idUser, String username, String password, String biographie, boolean isPro) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.biographie = biographie;
        this.isPro = isPro;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBiographie() {
        return biographie;
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsPro() {
        return isPro;
    }

    public void setIsPro(boolean isPro) {
        this.isPro = isPro;
    }
    
}
