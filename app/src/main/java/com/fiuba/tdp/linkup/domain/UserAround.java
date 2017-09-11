package com.fiuba.tdp.linkup.domain;

public class UserAround {
    private String id;
    private String picture;
    private String compatibility;
    private String description;
    private String userName;


    public UserAround(String id, String picture, String compatibility, String description, String userName) {
        this.id = id;
        this.picture = picture;
        this.compatibility = compatibility;
        this.description = description;
        this.userName = userName;
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

    public String getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(String compatibility) {
        this.compatibility = compatibility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
