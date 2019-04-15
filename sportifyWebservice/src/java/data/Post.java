/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Martin
 */
@XmlRootElement
public class Post implements Serializable{
    private int id;
    private String caption;
    private int creatorId;
    private Date timestamp;

    public Post(){
        
    }
    
    public Post(int postId, int creatorId, String caption, Date timestamp) {
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
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
        return this.id == other.id;
    }
    
}
