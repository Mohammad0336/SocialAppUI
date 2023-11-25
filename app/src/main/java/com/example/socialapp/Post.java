package com.example.socialapp;

// Post.java
import java.io.Serializable;

public class Post implements Serializable {

    private String userEmail;
    private String caption;
    private byte[] imageData;

    public Post(String userEmail, String caption, byte[] imageData) {
        this.userEmail = userEmail;
        this.caption = caption;
        this.imageData = imageData;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getCaption() {
        return caption;
    }

    public byte[] getImageData() {
        return imageData;
    }
}
