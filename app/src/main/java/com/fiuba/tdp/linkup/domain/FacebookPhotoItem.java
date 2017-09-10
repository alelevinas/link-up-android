package com.fiuba.tdp.linkup.domain;

/**
 * Created by alejandro on 9/10/17.
 */

public class FacebookPhotoItem {
    String id;
    String picture;
    String source;

    public FacebookPhotoItem(String id, String picture, String source) {
        this.id = id;
        this.picture = picture;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public String getPicture() {
        return picture;
    }

    public String getSource() {
        return source;
    }
}
