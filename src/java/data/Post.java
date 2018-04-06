/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.time.LocalDate;
import java.util.TreeSet;

/**
 *
 * @author Martin
 */
public class Post {
    private int idPost;
    private String caption;
    private int idCreator;
    private LocalDate timestamp;
    private TreeSet<Comment> comments = new TreeSet<Comment>();
    private TreeSet<Integer> likes = new TreeSet<Integer>();

    public Post(int idPost, String caption, int likes, LocalDate creationDate) {
        this.idPost = idPost;
        this.caption = caption;
        this.idCreator = likes;
        this.timestamp = creationDate;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String Caption) {
        this.caption = Caption;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }
    
}
