package com.midterm.group1.herolist;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private byte[] image;

    User(String username, String password, byte[] image){
        this.username = username;
        this.password = password;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
}
