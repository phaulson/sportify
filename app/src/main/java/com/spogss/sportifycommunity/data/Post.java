package com.spogss.sportifycommunity.data;

/**
 * Created by Pauli on 26.03.2018.
 */

public class Post {
    private static int nextID = 0;
    private int id;
    private String timeStamp;
    private String caption;
    private boolean liked;
    private int postPic;
    private int likes;
    private User user;

    public Post(String timeStamp, String caption, boolean liked, int postPic, int likes, User user) {
        this.user = user;
        this.timeStamp = timeStamp;
        this.caption = caption;
        this.liked = liked;
        this.postPic = postPic;
        this.likes = likes;
        this.id = nextID++;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getPostPic() {
        return postPic;
    }

    public void setPostPic(int pic) {
        this.postPic = pic;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public static int getNextID() {
        return nextID;
    }
}
