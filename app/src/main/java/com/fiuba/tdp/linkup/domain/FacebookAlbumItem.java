package com.fiuba.tdp.linkup.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alejandro on 9/10/17.
 */

public class FacebookAlbumItem {
    public String id;
    public String name;

    @SerializedName("cover_photo")
    public CoverPhoto coverPhoto;

    public FacebookAlbumItem(String id, String name, CoverPhoto coverPhoto) {
        this.id = id;
        this.name = name;
        this.coverPhoto = coverPhoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoverPhoto getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(CoverPhoto coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    @Override
    public String toString() {
        return name;
    }

    public class CoverPhoto {
        public String picture;

        public CoverPhoto(String picture) {
            this.picture = picture;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }
}
