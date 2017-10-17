package com.fiuba.tdp.linkup.domain;

/**
 * Created by alejandro on 10/16/17.
 */

public class LinkUpBlockedUser {
    String userId;
    String name;
    String description;
    String picture;

    public LinkUpBlockedUser(String userId, String name, String description, String picture) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.picture = picture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
