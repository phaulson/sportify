package com.spogss.sportifycommunity.feed;

/**
 * Created by Pauli on 26.03.2018.
 */

public class Post {
    private static int nextID = 0;
    private int id;
    private String username;
    private String timeStamp;
    private String caption;
    private boolean liked;
    private int postPic;
    private int profilePic;
    private int likes;

    public Post(String username, String timeStamp, String caption, boolean liked, int profilePic, int postPic, int likes) {
        this.username = username;
        this.timeStamp = timeStamp;
        this.caption = caption;
        this.liked = liked;
        this.postPic = postPic;
        this.profilePic = profilePic;
        this.likes = likes;
        this.id = nextID++;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
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
}
