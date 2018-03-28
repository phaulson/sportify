package pkgData;

import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author schueler
 */
public class User {
    private int idUser;
    private String username;
    private String password;
    private String biographie;
    private boolean pro;
    private TreeSet<Plan> subPlans = new TreeSet<Plan>();
    private TreeSet<Integer> followedUser = new TreeSet<Integer>();
    private TreeSet<Post> posts = new TreeSet<Post>();

    public User(int idUser, String username, String password, String biographie, boolean pro) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.biographie = biographie;
        this.pro = pro;
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

    public boolean isPro() {
        return pro;
    }

    public void setPro(boolean pro) {
        this.pro = pro;
    }
    
}
