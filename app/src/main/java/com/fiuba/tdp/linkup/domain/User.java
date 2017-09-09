package com.fiuba.tdp.linkup.domain;

/**
 * Created by alejandro on 9/9/17.
 */

public class User {
    private String id;
    private String userName;
    private String gender;
    private String picture;
//    private String education;
//    private String likes;


    public User(String id, String userName, String gender, String picture) {
        this.id = id;
        this.userName = userName;
        this.gender = gender;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
