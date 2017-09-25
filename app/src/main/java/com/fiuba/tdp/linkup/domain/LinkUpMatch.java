package com.fiuba.tdp.linkup.domain;

/**
 * Created by alejandro on 9/24/17.
 */

public class LinkUpMatch {
    String id;
    String name;
    String picture;
    String dateOfLink;

    public LinkUpMatch(String id, String name, String picture, String dateOfLink) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.dateOfLink = dateOfLink;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public String getDateOfLink() {
        return dateOfLink;
    }
}
