package com.fiuba.tdp.linkup.domain;

/**
 * Created by alejandro on 9/25/17.
 */

public class Match {
    Boolean link;
    String extra;

    public Match(Boolean link, String extra) {
        this.link = link;
        this.extra = extra;
    }

    public Boolean getLink() {
        return link;
    }

    public String getExtra() {
        return extra;
    }
}
