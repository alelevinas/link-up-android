package com.fiuba.tdp.linkup.domain;

public class UserAround {
    private String id;
    private String picture;
    private String like;
    private String superlike;
    private String description;
    private String name;


    public UserAround(String id, String name, String picture, String description, String like, String superlike) {
        this.id = id;
        this.picture = picture;
        this.like = like;
        this.description = description;
        this.name = name;
        this.superlike = superlike;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String userName) {
        this.name = userName;
    }

    public String getSuperlike() {
        return superlike;
    }

    public void setSuperlike(String superlike) {
        this.superlike = superlike;
    }
}
