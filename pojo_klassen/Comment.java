package pkgData;


import java.time.LocalDate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author schueler
 */
public class Comment {
    private int idComment;
    private int idPost;
    private int idCreator;
    private String text;
    private LocalDate timestamp;

    public Comment(int idComment, int idPost, int idCreator, String text, LocalDate timestamp) {
        this.idComment = idComment;
        this.idPost = idPost;
        this.idCreator = idCreator;
        this.text = text;
        this.timestamp = timestamp;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Comment{" + "idComment=" + idComment + ", idPost=" + idPost + ", idCreator=" + idCreator + ", text=" + text + ", timestamp=" + timestamp + '}';
    }
    
}
