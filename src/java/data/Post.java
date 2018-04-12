/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.TreeSet;

/**
 *
 * @author Martin
 */
public class Post implements Serializable{
    private int id;
    private String caption;
    private int creatorId;
    private LocalDate timestamp;

    public Post(int postId, int creatorId, String caption, LocalDate timestamp) {
        this.id = postId;
        this.caption = caption;
        this.creatorId = creatorId;
        this.timestamp = timestamp;
    }

    public int getPostId() {
        return id;
    }

    public void setPostId(int postId) {
        this.id = postId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Post other = (Post) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
}
